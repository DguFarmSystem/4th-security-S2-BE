package org.farmsystem.sotserver.domain.user.dto.response;

public record UserTokenResponseDTO(String accessToken) {
    public static UserTokenResponseDTO of(String accessToken) {
        return new UserTokenResponseDTO(accessToken);
    }
}

