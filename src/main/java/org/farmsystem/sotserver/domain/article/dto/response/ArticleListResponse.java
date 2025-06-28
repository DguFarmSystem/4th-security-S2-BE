package org.farmsystem.sotserver.domain.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.global.s3.S3Uploader;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleListResponse {
    private Long id;
    private String title;
    private String content;
    private ArticleStatus status;
    private Long userId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<String> imageUrls;

    // 댓글 개수 필드
    private int commentCount;
    // 좋아요 개수
    private Long likeCount;
    // 본인 좋아요 여부
    private Boolean isLiked;

    // 팩토리 메서드 (기존)
    public static ArticleListResponse from(Article article, S3Uploader s3Uploader, int commentCount) {
        return ArticleListResponse.builder()
                .id(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .status(article.getStatus())
                .userId(article.getAuthor().getUserId())
                .createAt(article.getCreatedAt())
                .updateAt(article.getUpdatedAt())
                .imageUrls(article.getImages().stream()
                        .map(img -> s3Uploader.getFileUrl(img.getKey()))
                        .toList())
                .commentCount(commentCount)
                .likeCount(null)
                .isLiked(null)
                .build();
    }
    // 팩토리 메서드 (좋아요 정보 포함)
    public static ArticleListResponse from(Article article, S3Uploader s3Uploader, int commentCount, Long likeCount, Boolean isLiked) {
        return ArticleListResponse.builder()
                .id(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .status(article.getStatus())
                .userId(article.getAuthor().getUserId())
                .createAt(article.getCreatedAt())
                .updateAt(article.getUpdatedAt())
                .imageUrls(article.getImages().stream()
                        .map(img -> s3Uploader.getFileUrl(img.getKey()))
                        .toList())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}