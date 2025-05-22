package org.farmsystem.sotserver.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.dto.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.ArticleResponse;
import org.farmsystem.sotserver.domain.article.service.ArticleService;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    //@PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid ArticleCreateRequest request
            ) {
        Long userId = userDetails.getUserId();
        ArticleResponse response = articleService.createArticle(request, userId);
        return ResponseEntity.ok(response);
    }
}
