package org.farmsystem.sotserver.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.comment.dto.CommentCreateRequest;
import org.farmsystem.sotserver.domain.comment.dto.CommentResponse;
import org.farmsystem.sotserver.domain.comment.entity.Comment;
import org.farmsystem.sotserver.domain.comment.repository.CommentRepository;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.domain.user.repository.UserRepository;
import org.farmsystem.sotserver.global.error.exception.EntityNotFoundException;
import org.farmsystem.sotserver.global.error.exception.ForbiddenException;

import static org.farmsystem.sotserver.global.error.ErrorCode.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long articleId, Long userId, CommentCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .article(article)
                .build();
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, CommentCreateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND));

        if(!comment.getUser().getUserId().equals(userId))
        {
            throw new ForbiddenException(COMMENT_AUTHOR_ONLY_ACTION);
        }

        comment.updateComment(request.getContent());

        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByArticle(Long articleId) {
        List<Comment> comments = commentRepository.findByArticle_ArticleId(articleId);
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException(COMMENT_AUTHOR_ONLY_ACTION);
        }
        commentRepository.delete(comment);
    }
}
