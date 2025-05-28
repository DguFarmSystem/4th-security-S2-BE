package org.farmsystem.sotserver.domain.article.repository;

import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthorOrderByCreatedAtDesc(User user);
}
