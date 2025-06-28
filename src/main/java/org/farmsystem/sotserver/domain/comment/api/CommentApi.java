package org.farmsystem.sotserver.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.farmsystem.sotserver.domain.comment.dto.CommentCreateRequest;
import org.farmsystem.sotserver.domain.comment.dto.CommentResponse;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.farmsystem.sotserver.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API", description = "댓글 관련 API 명세서")
public interface CommentApi {

    @Operation(summary = "댓글 생성", description = "댓글을 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "댓글 생성 성공",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글/사용자",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> createComment(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request
    );

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> deleteComment(
            @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "게시글의 댓글 목록 조회", description = "게시글에 달린 댓글 목록을 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))))
    ResponseEntity<SuccessResponse<?>> getComments(
            @PathVariable Long articleId
    );

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                            content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "수정 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> updateComment(
            @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreateRequest request
    );
}
