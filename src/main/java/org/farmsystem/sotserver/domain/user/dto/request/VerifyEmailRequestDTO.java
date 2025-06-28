package org.farmsystem.sotserver.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String code
) {}

