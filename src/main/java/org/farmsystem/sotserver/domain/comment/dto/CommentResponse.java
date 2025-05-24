package org.farmsystem.sotserver.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.farmsystem.sotserver.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long userId;
    private Long articleId;
    private String userName;
    private LocalDateTime createdAt;
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
