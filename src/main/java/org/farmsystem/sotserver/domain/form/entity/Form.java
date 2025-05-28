package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "form")
@Entity
public class Form extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formId;

    @OneToOne
    @JoinColumn(name = "article_id", unique = true, nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "form")
    private List<AnswerForm> answerForms;

    // Form에서 질문 리스트 조회 위한 양방향 매핑 추가
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormQuestion> questions;

    //기본 질문을 포함한 Form 생성
    public static Form createWithDefaultQuestions(Article article, User user) {
        Form form = Form.builder()
                .article(article)
                .user(user)
                .questions(new ArrayList<>())
                .build();

        form.addQuestion("이름", 0);
        form.addQuestion("연락처", 1);
        return form;
    }

    // 커스텀 질문 추가
    public void addQuestion(String content, int order) {
        FormQuestion question = FormQuestion.builder()
                .questionContent(content)
                .questionOrder(order)
                .form(this)
                .build();
        this.questions.add(question);
    }

}
