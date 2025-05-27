package org.farmsystem.sotserver.domain.form.repository;

import org.farmsystem.sotserver.domain.form.entity.AnswerForm;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerFormRepository extends JpaRepository<AnswerForm, Long> {

    List<AnswerForm> findAllByFormOrderByCreatedAtDesc(Form form);
}
