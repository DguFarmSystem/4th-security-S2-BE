package org.farmsystem.sotserver.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.domain.article.entity.Image;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private ArticleStatus status;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<String> imageUrls;

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getUserId(),
                article.getStatus(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getImages().stream()
                        .map(Image::getImageUrl)
                        .toList()
        );
    }
}
