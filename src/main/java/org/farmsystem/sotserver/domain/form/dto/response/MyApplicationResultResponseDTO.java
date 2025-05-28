package org.farmsystem.sotserver.domain.form.dto.response;

import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.form.entity.FormStatus;

public record MyApplicationResultResponseDTO(
        Long articleId,
        String imageUrl,
        FormStatus formStatus // WAITING(지원완료), APPROVED(선정), REJECTED(미선정)
){
    public static MyApplicationResultResponseDTO from (Article article, FormStatus formStatus){
        return new MyApplicationResultResponseDTO(
                article.getArticleId(),
                article.getThumbnailImage(),
                formStatus
        );
    }
}