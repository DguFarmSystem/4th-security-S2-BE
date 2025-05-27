package org.farmsystem.sotserver.domain.form.repository;

import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long> {
    boolean existsByArticle(Article article);
}
