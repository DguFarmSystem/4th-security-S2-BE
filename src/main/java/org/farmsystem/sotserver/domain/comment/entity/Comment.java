package org.farmsystem.sotserver.domain.comment.entity;

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
@Table(name = "comment")
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateComment(String content){
        this.content = content;
    }
}

