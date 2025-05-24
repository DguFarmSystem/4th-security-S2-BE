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
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private ArticleStatus status;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    List<String> imageUrls;

    public static ArticleResponse from(Article article, S3Uploader s3Uploader) {
        return new ArticleResponse(
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
