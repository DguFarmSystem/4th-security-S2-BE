package org.farmsystem.sotserver.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
