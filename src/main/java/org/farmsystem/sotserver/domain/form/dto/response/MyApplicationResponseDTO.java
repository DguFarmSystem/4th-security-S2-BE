package org.farmsystem.sotserver.domain.form.dto.response;

import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.form.entity.AnswerForm;
import org.farmsystem.sotserver.domain.form.entity.AnswerFormStatus;

import java.time.format.DateTimeFormatter;

public record MyApplicationResponseDTO(
        Long articleId,
        String imageUrl,
        String title,
        Long applicationId,
        String createdDate,
        AnswerFormStatus answerFormStatus // SAVING(임시저장), SUBMITTED(제출완료)
) {
    public static MyApplicationResponseDTO from(Article article, AnswerForm answerForm){
        String formattedDate = answerForm.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        return new MyApplicationResponseDTO(
                article.getArticleId(),
                article.getThumbnailImage(),
                article.getTitle(),
                answerForm.getApplicationId(),
                formattedDate,
                answerForm.getAnswerFormStatus()
        );
    }
}
