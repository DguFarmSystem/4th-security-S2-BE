package org.farmsystem.sotserver.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.user.dto.request.EmailRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.request.VerifyEmailRequestDTO;
import org.farmsystem.sotserver.domain.user.service.MailService;
import org.farmsystem.sotserver.domain.user.service.UserService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.farmsystem.sotserver.global.error.ErrorCode;
import org.farmsystem.sotserver.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<SuccessResponse<?>> sendEmailAuthCode(@RequestBody @Valid EmailRequestDTO request) {
        mailService.sendEmailAuthCode(request.email());
        return SuccessResponse.ok("인증번호 전송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<SuccessResponse<?>> verifyEmailCode(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid VerifyEmailRequestDTO request
    ) {
        mailService.verifyAuthCode(request.email(), request.code());
        userService.verifyUserEmail(userDetails.getUserId(), request.email());
        return SuccessResponse.ok("이메일 인증 성공");
    }

}

