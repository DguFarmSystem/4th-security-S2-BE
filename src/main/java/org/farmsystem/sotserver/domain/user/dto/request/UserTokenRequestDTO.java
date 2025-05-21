package org.farmsystem.sotserver.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserTokenRequestDTO(
        @NotBlank
        String accessToken
) {
}
