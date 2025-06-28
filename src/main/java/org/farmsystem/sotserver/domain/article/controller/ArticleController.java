package org.farmsystem.sotserver.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleStatusRequest;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleCreateResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleDetailResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleListResponse;
import org.farmsystem.sotserver.domain.article.service.ArticleService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        ArticleCreateResponse response = articleService.createArticle(userId, images, request);
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
        ArticleCreateResponse response = articleService.updateArticle(articleId, userId, images, request);
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
        ArticleCreateResponse response = articleService.changeStatus(articleId, userId, request);
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

    // 좋아요 정보 포함: 인증된 사용자만 userId 전달, 비로그인 시 null
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getArticles(
            @PageableDefault(size = 10, sort = "articleId", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        Page<ArticleListResponse> articles = articleService.getArticles(pageable, userId);
        return SuccessResponse.ok(articles);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<SuccessResponse<?>> getArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        ArticleDetailResponse response = articleService.getArticle(articleId, userId);
        return SuccessResponse.ok(response);
    }

    // 게시글 좋아요
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{articleId}/like")
    public ResponseEntity<SuccessResponse<?>> likeArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        articleService.likeArticle(articleId, userDetails.getUserId());
        return SuccessResponse.ok(null);
    }

    // 게시글 좋아요 취소
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{articleId}/like")
    public ResponseEntity<SuccessResponse<?>> unlikeArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        articleService.unlikeArticle(articleId, userDetails.getUserId());
        return SuccessResponse.ok(null);
    }

    // 내가 좋아요한 게시글 목록
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/liked")
    public ResponseEntity<SuccessResponse<?>> getLikedArticles(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ArticleListResponse> response = articleService.getLikedArticleResponses(userDetails.getUserId());
        return SuccessResponse.ok(response);
    }
}
