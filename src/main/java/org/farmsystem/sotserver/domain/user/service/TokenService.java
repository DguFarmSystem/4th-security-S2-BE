package org.farmsystem.sotserver.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.user.dto.request.UserTokenRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.config.auth.jwt.JwtProvider;
import org.farmsystem.sotserver.global.error.exception.EntityNotFoundException;
import org.farmsystem.sotserver.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.farmsystem.sotserver.global.error.ErrorCode.JSON_PARSING_FAILED;
import static org.farmsystem.sotserver.global.error.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public String issueNewAccessToken(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        return jwtProvider.getIssueToken(userId, true);
    }

    public UserTokenResponseDTO issueTempToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        return UserTokenResponseDTO.of(accessToken);
    }

    public UserTokenResponseDTO issueToken(Long userId) {
        String accessToken = jwtProvider.getIssueToken(userId, true);
        return UserTokenResponseDTO.of(accessToken);
    }

    public UserTokenResponseDTO reissue(UserTokenRequestDTO userTokenRequest) {
        Long userId;
        try {
            userId = Long.valueOf(jwtProvider.decodeJwtPayloadSubject(userTokenRequest.accessToken()));
        } catch (JsonProcessingException e) {
            throw new UnauthorizedException(JSON_PARSING_FAILED);
        }

        String newAccessToken = issueNewAccessToken(userId);
        return UserTokenResponseDTO.of(newAccessToken);
    }

    public void logout(Long userId) {
        // 로그아웃 시 특별히 처리할 내용 없음 (리프레시 토큰 없음)
    }
}
