package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import org.farmsystem.sotserver.domain.user.entity.User;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private String answerContent;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private FormQuestion question;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
