package org.farmsystem.sotserver.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.farmsystem.sotserver.domain.user.dto.request.UserLoginRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.entity.UserStatus;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.config.auth.jwt.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public User saveUser(String socialId, String imageUrl, UserLoginRequestDTO request) {
        return userRepository.findBySocialId(socialId)
                .orElseGet(() -> userRepository.save(
                        new User(socialId, imageUrl, UserStatus.ACTIVE)
                ));
    }

    public UserTokenResponseDTO issueTempToken(Long userId) {
        String accessToken = jwtProvider.getIssueToken(userId, true);
        return UserTokenResponseDTO.of(accessToken);
    }
}
