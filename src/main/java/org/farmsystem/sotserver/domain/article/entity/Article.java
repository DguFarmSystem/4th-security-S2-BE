package org.farmsystem.sotserver.domain.article.entity;

import jakarta.persistence.*;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.farmsystem.sotserver.domain.form.entity.FormStatus;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

@Entity
public class Article extends BaseTimeEntity {

    @Id
    private Long articleId;

    @Enumerated(EnumType.STRING)
    private FormStatus formStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
