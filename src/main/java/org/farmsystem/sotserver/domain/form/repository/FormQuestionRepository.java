package org.farmsystem.sotserver.domain.form.repository;

import org.farmsystem.sotserver.domain.form.entity.FormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormQuestionRepository extends JpaRepository<FormQuestion, Long> {
}
