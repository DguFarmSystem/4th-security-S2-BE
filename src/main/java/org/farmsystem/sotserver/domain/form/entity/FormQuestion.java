package org.farmsystem.sotserver.domain.form.entity;

import jakarta.persistence.*;

@Entity
public class FormQuestion {
    @Id
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
}

