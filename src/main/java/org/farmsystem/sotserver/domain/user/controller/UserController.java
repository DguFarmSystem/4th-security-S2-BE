package org.farmsystem.sotserver.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.user.dto.request.ProfileUpdateRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.request.UpdateNicknameRequest;
import org.farmsystem.sotserver.domain.user.dto.response.MypageResponseDTO;
import org.farmsystem.sotserver.domain.user.service.UserService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    //마이페이지 조회
    @GetMapping("/my-page")
    public ResponseEntity<SuccessResponse<?>> getMyPage(
            @AuthenticationPrincipal(expression = "userId") Long userId)
    {
        MypageResponseDTO myPage = userService.getMypage(userId);
        return SuccessResponse.ok(myPage);
    }


    //마이페이지 수정
    @PatchMapping("/my-page")
    public ResponseEntity<SuccessResponse<?>> updateProfile(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @ModelAttribute ProfileUpdateRequestDTO profileUpdateRequest
    ) {
        userService.updateProfile(userId, profileUpdateRequest);
        return SuccessResponse.ok(null);
    }

    // 닉네임 수정 (최초 회원가입 시에도 사용)
    @PatchMapping("/nickname")
    public ResponseEntity<SuccessResponse<?>> updateNickname(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestBody UpdateNicknameRequest request) {
        userService.updateNickname(userDetails.getUserId(), request.nickname());
        return SuccessResponse.ok(null);
    }
}
