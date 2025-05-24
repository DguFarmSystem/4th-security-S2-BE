package org.farmsystem.sotserver.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.global.s3.S3Uploader;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleCreateResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private ArticleStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<String> imageUrls;

    public static ArticleCreateResponse from(Article article, S3Uploader s3Uploader) {
        return new ArticleCreateResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getUserId(),
                article.getStatus(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getImages().stream()
                        .map(img -> s3Uploader.getFileUrl(img.getKey()))
                        .toList()
        );
    }
}
