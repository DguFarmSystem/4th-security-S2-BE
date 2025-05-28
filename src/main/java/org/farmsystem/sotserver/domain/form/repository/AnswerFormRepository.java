package org.farmsystem.sotserver.domain.form.repository;

import org.farmsystem.sotserver.domain.form.entity.AnswerForm;
import org.farmsystem.sotserver.domain.form.entity.AnswerFormStatus;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerFormRepository extends JpaRepository<AnswerForm, Long> {
    List<AnswerForm> findAllByFormAndAnswerFormStatusOrderByCreatedAtDesc(Form form, AnswerFormStatus answerFormStatus);

    List<AnswerForm> findByUserOrderByCreatedAtDesc(User user);
}
