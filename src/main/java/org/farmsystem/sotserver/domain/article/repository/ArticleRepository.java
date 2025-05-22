package org.farmsystem.sotserver.domain.article.repository;

import org.farmsystem.sotserver.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
