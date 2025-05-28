package org.farmsystem.sotserver.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.user.dto.request.ProfileUpdateRequestDTO;
import org.farmsystem.sotserver.domain.user.dto.response.MypageResponseDTO;
import org.farmsystem.sotserver.domain.user.service.UserService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
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
}
