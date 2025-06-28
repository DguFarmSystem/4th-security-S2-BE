package org.farmsystem.sotserver.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.entity.ArticleStatus;
import org.farmsystem.sotserver.global.s3.S3Uploader;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "게시글 생성 응답 DTO")
public class ArticleCreateResponse {
    @Schema(description = "게시글 ID", example = "1")
    private Long id;
    @Schema(description = "제목", example = "제목입니다")
    private String title;
    @Schema(description = "내용", example = "내용입니다")
    private String content;
    @Schema(description = "작성자 ID", example = "2")
    private Long userId;
    @Schema(description = "게시글 상태", example = "OPEN")
    private ArticleStatus status;
    @Schema(description = "생성일시", example = "2024-06-29T12:34:56")
    private LocalDateTime createAt;
    @Schema(description = "수정일시", example = "2024-06-29T12:34:56")
    private LocalDateTime updateAt;
    @Schema(description = "이미지 URL 목록")
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
