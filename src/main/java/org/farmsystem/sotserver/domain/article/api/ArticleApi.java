package org.farmsystem.sotserver.domain.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleCreateRequest;
import org.farmsystem.sotserver.domain.article.dto.request.ArticleStatusRequest;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleCreateResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleDetailResponse;
import org.farmsystem.sotserver.domain.article.dto.response.ArticleListResponse;
import org.farmsystem.sotserver.global.common.SuccessResponse;
import org.farmsystem.sotserver.global.config.auth.CustomUserDetails;
import org.farmsystem.sotserver.global.error.dto.ErrorResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "게시글 API", description = "게시글 관련 API 명세서")
public interface ArticleApi {

    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시글 생성 성공",
                            content = @Content(schema = @Schema(implementation = ArticleCreateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> createArticle(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("data") ArticleCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    );

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                            content = @Content(schema = @Schema(implementation = ArticleCreateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "수정 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> updateArticle(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("data") ArticleCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    );

    @Operation(summary = "게시글 상태 변경", description = "게시글의 상태를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                            content = @Content(schema = @Schema(implementation = ArticleCreateResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "변경 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> changeStatus(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ArticleStatusRequest request
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> deleteArticle(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ArticleListResponse.class))))
    ResponseEntity<SuccessResponse<?>> getArticles(
            @PageableDefault(size = 10) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> getArticle(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 누릅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글/사용자",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "이미 좋아요를 누른 게시글",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> likeArticle(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "게시글 좋아요 취소", description = "게시글에 누른 좋아요를 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글/사용자",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> unlikeArticle(
            @PathVariable Long articleId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "내가 좋아요한 게시글 목록 조회", description = "내가 좋아요한 게시글 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = ArticleListResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<SuccessResponse<?>> getLikedArticles(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );
}
