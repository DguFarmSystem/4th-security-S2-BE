package org.farmsystem.sotserver.domain.form.dto.response;

public record FormQuestionResponseDTO(
        int questionOrder,
        String questionContent

) {
    public static FormQuestionResponseDTO of(int questionOrder, String questionContent) {
        return new FormQuestionResponseDTO(questionOrder, questionContent);
    }
}
