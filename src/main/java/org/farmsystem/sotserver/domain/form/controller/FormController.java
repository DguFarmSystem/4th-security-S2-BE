package org.farmsystem.sotserver.domain.form.controller;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.form.dto.request.FormCreateRequestDTO;
import org.farmsystem.sotserver.domain.form.dto.response.FormQuestionResponseDTO;
import org.farmsystem.sotserver.domain.form.service.FormService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/form")
@RestController
public class FormController {

    private final FormService formService;

    // 폼 생성 (질문 생성)
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createForm(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @RequestBody FormCreateRequestDTO formCreateRequest){
        formService.createForm(userId, formCreateRequest);
        return SuccessResponse.created(null);
    }

    // 폼 질문 조회
    @GetMapping("/{formId}/question")
    public ResponseEntity<SuccessResponse<?>> getFormQuestions(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable Long formId ) {
        List<FormQuestionResponseDTO> formQuestions = formService.getFormQuestions(userId, formId);
        return SuccessResponse.ok(formQuestions);
    }

}
