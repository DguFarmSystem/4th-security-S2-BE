package org.farmsystem.sotserver.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.ArticleResponse;
import org.farmsystem.sotserver.domain.article.dto.ArticleStatusRequest;
import org.farmsystem.sotserver.domain.article.service.ArticleService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> createArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("data") @Valid ArticleCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        Long userId = userDetails.getUserId();
        ArticleResponse response = articleService.createArticle(userId, images, request);
        return SuccessResponse.created(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(value = "/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<?>> updateArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("data") @Valid ArticleCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long userId = userDetails.getUserId();
        ArticleResponse response = articleService.updateArticle(articleId, userId, images, request);
        return SuccessResponse.ok(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{articleId}/status")
    public ResponseEntity<SuccessResponse<?>> changeStatus(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid ArticleStatusRequest request
    ) {
        Long userId = userDetails.getUserId();
        ArticleResponse response = articleService.changeStatus(articleId, userId, request);
        return SuccessResponse.ok(response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<SuccessResponse<?>> deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        articleService.deleteArticle(articleId, userId);
        return SuccessResponse.noContent(); // ✅ 204 No Content
    }
}
