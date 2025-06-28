package org.farmsystem.sotserver.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleStatusRequest;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleCreateResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleDetailResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleListResponse;
import org.farmsystem.sotserver.domain.article.dto.response.MyArticleResponseDTO;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleLike;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.domain.article.entity.Image;
import org.farmsystem.sotserver.domain.article.repository.ArticleLikeRepository;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.article.repository.ImageRepository;
import org.farmsystem.sotserver.domain.comment.dto.CommentResponse;
import org.farmsystem.sotserver.domain.comment.entity.Comment;
import org.farmsystem.sotserver.domain.comment.repository.CommentRepository;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.farmsystem.sotserver.domain.form.entity.FormStatus;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.error.exception.ConflictException;
import org.farmsystem.sotserver.global.error.exception.EntityNotFoundException;
import org.farmsystem.sotserver.global.error.exception.ForbiddenException;
import org.farmsystem.sotserver.global.s3.S3Uploader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.farmsystem.sotserver.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;
    private final ArticleLikeRepository articleLikeRepository;

    public ArticleCreateResponse createArticle(Long userId, List<MultipartFile> images, ArticleCreateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ArticleStatus.OPEN)
                .author(user)
                .build();

        uploadImages(article, images);

        Article saved = articleRepository.save(article);

        return ArticleCreateResponse.from(saved, s3Uploader);
    }

    @Transactional
    public ArticleCreateResponse updateArticle(Long articleId, Long userId, List<MultipartFile> images, ArticleCreateRequest request){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new ForbiddenException(ARTICLE_AUTHOR_ONLY_ACTION);
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

        return ArticleCreateResponse.from(article, s3Uploader);
    }

    @Transactional
    public ArticleCreateResponse changeStatus(Long articleId, Long userId, ArticleStatusRequest request){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new ForbiddenException(ARTICLE_AUTHOR_ONLY_ACTION);
        }

        article.changeStatus(request.getStatus());

        return ArticleCreateResponse.from(article, s3Uploader);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long userId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        if (!article.getAuthor().getUserId().equals(userId)){
            throw new ForbiddenException(ARTICLE_AUTHOR_ONLY_ACTION);
        }

        List<Image> toDelete = new ArrayList<>(article.getImages());
        for (Image image : toDelete){
            s3Uploader.delete(image.getKey());
        }

        imageRepository.deleteAllByArticle(article);
        articleRepository.delete(article);
    }

    // userId를 받아 좋아요 정보 포함하여 반환하도록 수정
    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticle(Long articleId, Long userId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));
        List<Comment> commentList = commentRepository.findByArticle_ArticleId(articleId);
        List<CommentResponse> commentResponses = commentList.stream()
                .map(CommentResponse::from)
                .toList();
        Long likeCount = articleLikeRepository.countByArticle(article);
        Boolean isLiked = (userId != null) ? articleLikeRepository.findByArticleAndUser(article, userRepository.findById(userId).orElse(null)).isPresent() : false;
        return ArticleDetailResponse.from(article, s3Uploader, commentResponses, likeCount, isLiked);
    }

    // userId를 받아 좋아요 정보 포함하여 반환하도록 수정
    @Transactional(readOnly = true)
    public Page<ArticleListResponse> getArticles(Pageable pageable, Long userId){
        Page<Article> page = articleRepository.findAll(pageable);
        return page.map(article -> {
            int commentCount = commentRepository.countByArticle_ArticleId(article.getArticleId());
            Long likeCount = articleLikeRepository.countByArticle(article);
            Boolean isLiked = (userId != null) ? articleLikeRepository.findByArticleAndUser(article, userRepository.findById(userId).orElse(null)).isPresent() : false;
            return ArticleListResponse.from(article, s3Uploader, commentCount, likeCount, isLiked);
        });
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

    @Transactional(readOnly = true)
    public List<MyArticleResponseDTO> getMyArticles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        List<Article> articles = articleRepository.findByAuthorOrderByCreatedAtDesc(user);

        return articles.stream()
                .map(article -> {
                    boolean isAccepted = articleHasApprovedAnswerForm(article);
                    return MyArticleResponseDTO.from(article, isAccepted);
                })
                .toList();
    }

    //수락 완료인지 판별(임시용)
    private boolean articleHasApprovedAnswerForm(Article article) {
        Form form = article.getForm();
        return form.getAnswerForms().stream()
                .anyMatch(answerForm -> answerForm.getFormStatus() == FormStatus.APPROVED);
    }

    // 좋아요 등록
    @Transactional
    public void likeArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        if (articleLikeRepository.findByArticleAndUser(article, user).isPresent()) {
            throw new ConflictException(ALREADY_LIKED_ARTICLE);
        }
        ArticleLike like = ArticleLike.builder().article(article).user(user).build();
        articleLikeRepository.save(like);
    }

    // 좋아요 취소
    @Transactional
    public void unlikeArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        articleLikeRepository.deleteByArticleAndUser(article, user);
    }

    // 게시글의 좋아요 개수 반환
    @Transactional(readOnly = true)
    public Long getLikeCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));
        return articleLikeRepository.countByArticle(article);
    }

    // 사용자가 좋아요한 게시글 목록 반환
    @Transactional(readOnly = true)
    public List<Article> getLikedArticles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        List<ArticleLike> likes = articleLikeRepository.findAllByUser(user);
        return likes.stream().map(ArticleLike::getArticle).toList();
    }

    // 사용자가 해당 게시글에 좋아요를 눌렀는지 여부
    @Transactional(readOnly = true)
    public boolean isArticleLikedByUser(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return articleLikeRepository.findByArticleAndUser(article, user).isPresent();
    }

    // 내가 좋아요한 게시글 목록을 ArticleListResponse로 반환
    @Transactional(readOnly = true)
    public List<ArticleListResponse> getLikedArticleResponses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        List<ArticleLike> likes = articleLikeRepository.findAllByUser(user);
        return likes.stream().map(like -> {
            Article article = like.getArticle();
            int commentCount = commentRepository.countByArticle_ArticleId(article.getArticleId());
            Long likeCount = articleLikeRepository.countByArticle(article);
            return ArticleListResponse.from(
                    article,
                    s3Uploader,
                    commentCount,
                    likeCount,
                    true
            );
        }).toList();
    }
}
