package org.farmsystem.sotserver.domain.article.dto.response;

import org.farmsystem.sotserver.domain.article.entity.Article;

import java.time.format.DateTimeFormatter;

public record MyArticleResponseDTO(
        Long articleId,
        String imageUrl,
        String title,
        String createdDate,
        Long formId,
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
