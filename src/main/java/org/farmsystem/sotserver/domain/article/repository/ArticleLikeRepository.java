package org.farmsystem.sotserver.domain.article.repository;

import org.farmsystem.sotserver.domain.article.entity.ArticleLike;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleAndUser(Article article, User user);
    Long countByArticle(Article article);
    List<ArticleLike> findAllByUser(User user);
    void deleteByArticleAndUser(Article article, User user);
}

