package org.farmsystem.sotserver.domain.comment.repository;

import org.farmsystem.sotserver.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticle_ArticleId(Long articleId);
    int countByArticle_ArticleId(Long articleId);
}
