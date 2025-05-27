package org.farmsystem.sotserver.domain.form.dto.request;

import java.util.List;

public record FormCreateRequestDTO(
        Long articleId,
        List<String> formQuestions
) {
}
