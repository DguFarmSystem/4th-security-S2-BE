package org.farmsystem.sotserver.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.farmsystem.sotserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Schema(description = "댓글 응답 DTO")
public class CommentResponse {
    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;
    @Schema(description = "댓글 내용", example = "댓글입니다")
    private String content;
    @Schema(description = "작성자 ID", example = "2")
    private Long userId;
    @Schema(description = "게시글 ID", example = "1")
    private Long articleId;
    @Schema(description = "작성자 이름", example = "홍길동")
    private String userName;
    @Schema(description = "생성일시", example = "2024-06-29T12:34:56")
    private LocalDateTime createdAt;
    @Schema(description = "수정일시", example = "2024-06-29T12:34:56")
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .userId(comment.getUser().getUserId())
                .articleId(comment.getArticle().getArticleId())
                .userName(comment.getUser().getSocialId()) // 원하는 정보로 변경 가능
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
