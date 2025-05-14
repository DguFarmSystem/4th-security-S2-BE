package org.farmsystem.sotserver.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.global.config.auth.jwt.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final JwtProvider jwtProvider;

    public String issueNewAccessToken(Long userId) {
        return jwtProvider.getIssueToken(userId, true);
    }
    public UserTokenResponseDTO issueTempToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        return UserTokenResponseDTO.of(accessToken);
    }
}
