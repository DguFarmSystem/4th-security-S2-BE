package org.farmsystem.sotserver.domain.article.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "articles")
@Entity
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status = ArticleStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void changeStatus(ArticleStatus status) {
        this.status = status;
    }
}
