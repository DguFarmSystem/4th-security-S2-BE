package org.farmsystem.sotserver.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.farmsystem.sotserver.domain.article.entity.Article;

import java.time.format.DateTimeFormatter;

@Schema(description = "내 게시글 목록 응답 DTO")
public record MyArticleResponseDTO(
        @Schema(description = "게시글 ID", example = "1")
        Long articleId,
        @Schema(description = "썸네일 이미지 URL")
        String imageUrl,
        @Schema(description = "제목", example = "제목입니다")
        String title,
        @Schema(description = "생성일(yyyy.MM.dd)", example = "2024.06.29")
        String createdDate,
        @Schema(description = "폼 ID", example = "10")
        Long formId,
        @Schema(description = "수락 여부", example = "true")
        Boolean isAccepted // true(수락완료), false(지원자보기)
) {
    public static MyArticleResponseDTO from(Article article, Boolean isAccepted) {
        String formattedDate = article.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        return new MyArticleResponseDTO(
                article.getArticleId(),
                article.getThumbnailImage(),
                article.getTitle(),
                formattedDate,
                article.getForm().getFormId(),
                isAccepted
        );
    }

}
