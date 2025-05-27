package org.farmsystem.sotserver.domain.form.dto.response;

import org.farmsystem.sotserver.domain.form.entity.AnswerForm;
import org.farmsystem.sotserver.domain.form.entity.FormStatus;
import org.farmsystem.sotserver.domain.form.entity.ReadStatus;
import org.farmsystem.sotserver.domain.user.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record FormApplicationResponseDTO(
        Long applicationId,
        String createdDate,
        FormStatus formStatus,
        ReadStatus readStatus,
        String imageUrl,
        String nickname,
        List<String> skills,
        List<String> talents
) {
    public static FormApplicationResponseDTO from(AnswerForm answerForm, User user) {
        String formattedDate = answerForm.getCreatedAt().format(DateTimeFormatter.ofPattern("MM.dd"));

        return new FormApplicationResponseDTO(
                answerForm.getApplicationId(),
                formattedDate,
                answerForm.getFormStatus(),
                answerForm.getReadStatus(),
                user.getImageUrl(),
                user.getNickname(),
                user.getParsedList(user.getSkills()),
                user.getParsedList(user.getTalents())
        );
    }
}