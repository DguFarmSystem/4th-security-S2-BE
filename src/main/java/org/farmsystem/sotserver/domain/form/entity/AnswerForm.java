package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
public class AnswerForm extends BaseTimeEntity {
    @Id
    private Long applicationId;

    @Enumerated(EnumType.STRING)
    private FormStatus formStatus;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

