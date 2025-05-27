package org.farmsystem.sotserver.domain.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.domain.comment.dto.CommentResponse;
import org.farmsystem.sotserver.global.s3.S3Uploader;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleDetailResponse {
    private Long id;
    private String title;
    private String content;
    private ArticleStatus status;
    private Long userId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<String> imageUrls;

    // 댓글 정보 필드 추가
    private List<CommentResponse> comments;

    public static ArticleDetailResponse from(Article article, S3Uploader s3Uploader, List<CommentResponse> comments) {
        return ArticleDetailResponse.builder()
                .id(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .userId(article.getAuthor().getUserId())
                .status(article.getStatus())
                .createAt(article.getCreatedAt())
                .updateAt(article.getUpdatedAt())
                .imageUrls(article.getImages().stream()
                        .map(img -> s3Uploader.getFileUrl(img.getKey()))
                        .toList())
                .comments(comments)
                .build();
    }
}
