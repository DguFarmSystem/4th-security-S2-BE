package org.farmsystem.sotserver.domain.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.user.dto.request.UserLoginRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.domain.user.service.OauthService;
import org.farmsystem.sotserver.domain.user.service.TokenService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final OauthService oauthService;
    private final TokenService tokenService;

    // 임시 토큰 발급 API
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getTempToken(@PathVariable Long userId) {
        UserTokenResponseDTO userToken = tokenService.issueTempToken(userId);
        return SuccessResponse.ok(userToken);
    }

    // 소셜 로그인
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<?>> socialLogin(@RequestBody @Valid UserLoginRequestDTO userLoginRequest) {
        UserTokenResponseDTO userToken = oauthService.socialLogin(userLoginRequest);
        return SuccessResponse.ok(userToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<?>> logout(@AuthenticationPrincipal Long userId) {
        tokenService.logout(userId);
        return SuccessResponse.ok(null);
    }

}