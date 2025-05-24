package org.farmsystem.sotserver.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.ArticleResponse;
import org.farmsystem.sotserver.domain.article.dto.ArticleStatusRequest;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.domain.article.entity.Image;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.article.repository.ImageRepository;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.s3.S3Uploader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public ArticleResponse createArticle(Long userId, List<MultipartFile> images, ArticleCreateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ArticleStatus.OPEN)
                .author(user)
                .build();

        uploadImages(article, images);

        Article saved = articleRepository.save(article);

        return ArticleResponse.from(saved, s3Uploader);
    }

    @Transactional
    public ArticleResponse updateArticle(Long articleId, Long userId, List<MultipartFile> images, ArticleCreateRequest request){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new SecurityException("본인의 글만 수정할 수 있습니다");
        }

        article.update(request.getTitle(), request.getContent());

        // S3에서 이미지 삭제
        List<Image> toDelete = new ArrayList<>(article.getImages());
        for (Image image : toDelete){
            s3Uploader.delete(image.getKey());
        }

        // DB 이미지 삭제 후 S3 새 이미지 등록
        imageRepository.deleteAllByArticle(article);
        article.getImages().clear();
        uploadImages(article, images);

        return ArticleResponse.from(article, s3Uploader);
    }

    @Transactional
    public ArticleResponse changeStatus(Long articleId, Long userId, ArticleStatusRequest request){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new SecurityException("본인의 글만 상태를 변경할 수 있습니다.");
        }

        article.changeStatus(request.getStatus());

        return ArticleResponse.from(article, s3Uploader);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long userId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new SecurityException("본인의 글만 삭제할 수 있습니다.");
        }

        List<Image> toDelete = new ArrayList<>(article.getImages());
        for (Image image : toDelete){
            s3Uploader.delete(image.getKey());
        }

        imageRepository.deleteAllByArticle(article);
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return ArticleResponse.from(article, s3Uploader);
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> getArticles(Pageable pageable){
        return articleRepository.findAll(pageable)
                .map(article -> ArticleResponse.from(article, s3Uploader));
    }

    private void uploadImages(Article article, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) return;

        for (MultipartFile file : images) {
            try {
                if (file == null || file.isEmpty()) continue;
                String key = s3Uploader.upload(file, "articles");
                Image image = new Image(key, article);
                article.getImages().add(image);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }
    }
}
