package org.farmsystem.sotserver.domain.user.controller;

import lombok.RequiredArgsConstructor;

import org.farmsystem.sotserver.domain.user.dto.response.UserTokenResponseDTO;
import org.farmsystem.sotserver.domain.user.service.UserService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;
}
