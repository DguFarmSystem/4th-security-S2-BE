package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "form_question")
@Entity
public class FormQuestion {
    @Id
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
}

