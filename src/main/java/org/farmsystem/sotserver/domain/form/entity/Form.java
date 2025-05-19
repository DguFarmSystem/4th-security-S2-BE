package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "form")
@Entity
public class Form extends BaseTimeEntity {
    @Id
    private Long formId;

    @Enumerated(EnumType.STRING)
    private FormStatus formStatus;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
