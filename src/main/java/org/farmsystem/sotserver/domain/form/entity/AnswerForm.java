package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.common.BaseTimeEntity;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "answer_form")
@Entity
public class AnswerForm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Enumerated(EnumType.STRING)
    private FormStatus formStatus = FormStatus.WAITING;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus = ReadStatus.UNREAD;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updateFormStatus(FormStatus formStatus) {
        this.formStatus = formStatus;
    }
}

