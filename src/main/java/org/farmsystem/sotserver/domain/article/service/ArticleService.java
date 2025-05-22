package org.farmsystem.sotserver.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.ArticleResponse;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleResponse createArticle(ArticleCreateRequest request,Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ArticleStatus.OPEN)
                .author(user)
                .build();

        Article saved = articleRepository.save(article);

        return new ArticleResponse(
                saved.getArticleId(),
                saved.getTitle(),
                saved.getContent(),
                user.getUserId()
        );
    }

}
