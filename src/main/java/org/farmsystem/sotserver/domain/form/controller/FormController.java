package org.farmsystem.sotserver.domain.form.controller;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.form.dto.request.FormCreateRequestDTO;
import org.farmsystem.sotserver.domain.form.service.FormService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/form")
@RestController
public class FormController {

    private final FormService formService;
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createForm(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @RequestBody FormCreateRequestDTO formCreateRequest){
        formService.createForm(userId, formCreateRequest);
        return SuccessResponse.created(null);
    }

}
