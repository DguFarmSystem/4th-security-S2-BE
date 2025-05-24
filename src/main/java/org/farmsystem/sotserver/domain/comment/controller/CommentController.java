package org.farmsystem.sotserver.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.comment.dto.CommentCreateRequest;
import org.farmsystem.sotserver.domain.comment.dto.CommentResponse;
import org.farmsystem.sotserver.domain.comment.service.CommentService;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/article/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("{articleId}/comments")
    public ResponseEntity<SuccessResponse<?>> createComment(
            @PathVariable("articleId") Long articleId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        Long userId = userDetails.getUserId();
        CommentResponse commentResponse = commentService.createComment(articleId, userId, request);
        return SuccessResponse.created(commentResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("comments/{commentId}")
    public ResponseEntity<SuccessResponse<?>> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        Long userId = userDetails.getUserId();
        CommentResponse commentResponse =  commentService.updateComment(commentId, userId, request);
        return SuccessResponse.ok(commentResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("comments/{commentId}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getUserId());
        return SuccessResponse.noContent();
    }
}
