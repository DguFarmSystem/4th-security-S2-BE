package org.farmsystem.sotserver.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.global.s3.S3Uploader;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 목록 응답 DTO")
public class ArticleListResponse {
    @Schema(description = "게시글 ID", example = "1")
    private Long id;
    @Schema(description = "제목", example = "제목입니다")
    private String title;
    @Schema(description = "내용", example = "내용입니다")
    private String content;
    @Schema(description = "게시글 상태", example = "OPEN")
    private ArticleStatus status;
    @Schema(description = "작성자 ID", example = "2")
    private Long userId;
    @Schema(description = "생성일시", example = "2024-06-29T12:34:56")
    private LocalDateTime createAt;
    @Schema(description = "수정일시", example = "2024-06-29T12:34:56")
    private LocalDateTime updateAt;
    @Schema(description = "이미지 URL 목록")
    private List<String> imageUrls;

    // 댓글 개수 필드
    @Schema(description = "댓글 개수", example = "3")
    private int commentCount;
    // 좋아요 개수
    @Schema(description = "좋아요 개수", example = "5")
    private Long likeCount;
    // 본인 좋아요 여부
    @Schema(description = "본인 좋아요 여부", example = "true")
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