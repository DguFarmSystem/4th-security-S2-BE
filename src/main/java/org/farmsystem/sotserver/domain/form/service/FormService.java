package org.farmsystem.sotserver.domain.form.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.form.dto.request.FormCreateRequestDTO;
import org.farmsystem.sotserver.domain.form.dto.request.FormStatusRequestDTO;
import org.farmsystem.sotserver.domain.form.dto.response.FormApplicationResponseDTO;
import org.farmsystem.sotserver.domain.form.dto.response.FormQuestionResponseDTO;
import org.farmsystem.sotserver.domain.form.entity.AnswerForm;
import org.farmsystem.sotserver.domain.form.entity.Form;
import org.farmsystem.sotserver.domain.form.entity.FormStatus;
import org.farmsystem.sotserver.domain.form.entity.ReadStatus;
import org.farmsystem.sotserver.domain.form.repository.AnswerFormRepository;
import org.farmsystem.sotserver.domain.form.repository.FormRepository;
import org.farmsystem.sotserver.domain.user.entity.User;
import org.farmsystem.sotserver.global.error.exception.ConflictException;
import org.farmsystem.sotserver.global.error.exception.EntityNotFoundException;
import org.farmsystem.sotserver.global.error.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.farmsystem.sotserver.global.error.ErrorCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class FormService {

    private final ArticleRepository articleRepository;
    private final FormRepository formRepository;
    private final AnswerFormRepository answerFormRepository;

    // 작성자인지 검증
    private void validateAuthor(Long userId, Article article) {
        if (!article.getAuthor().getUserId().equals(userId)) {
            throw new ForbiddenException(ARTICLE_AUTHOR_ONLY_ACTION);
        }
    }

    // 폼 생성 (질문 생성)
    public void createForm(Long userId, FormCreateRequestDTO formCreateRequest) {
        Article article = articleRepository.findById(formCreateRequest.articleId())
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        User author = article.getAuthor();
        validateAuthor(userId, article);

        if (formRepository.existsByArticle(article)) {
            throw new ConflictException(ARTICLE_FORM_ALREADY_EXISTS);
        }

        Form form = Form.createWithDefaultQuestions(article, author);

        int order = 2;
        for (String question : formCreateRequest.formQuestions()) {
            form.addQuestion(question, order++);
        }

        formRepository.save(form);
    }

    // 폼 질문 조회
    public List<FormQuestionResponseDTO> getFormQuestions(Long userId, Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException(FORM_NOT_FOUND));

        return form.getQuestions().stream()
                .map(question -> FormQuestionResponseDTO.of(question.getQuestionOrder(), question.getQuestionContent()))
                .toList();
    }

    // 지원폼 목록 조회
    @Transactional(readOnly = true)
    public List<FormApplicationResponseDTO> getFormApplications(Long userId) {
        Form form = formRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(FORM_NOT_FOUND));

        List<AnswerForm> answerForms = answerFormRepository.findAllByFormOrderByCreatedAtDesc(form);

        return answerForms.stream()
                .map(answerForm -> FormApplicationResponseDTO.from(answerForm, answerForm.getUser()))
                .toList();
    }


    // 지원폼 수락/거절
    public void updateFormStatus(Long userId, Long applicationId, FormStatusRequestDTO formStatusRequest) {
        FormStatus formStatus = formStatusRequest.formStatus();

        AnswerForm answerForm = answerFormRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException(ANSWER_FORM_NOT_FOUND));

        // 작성자인지 검증
        Form form = answerForm.getForm();
        Article article = form.getArticle();
        validateAuthor(userId, article);

        answerForm.updateFormStatus(formStatus);
    }

    // 지원폼 열람
    public void readFormApplication(Long userId, Long applicationId) {
        AnswerForm answerForm = answerFormRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException(ANSWER_FORM_NOT_FOUND));

        Form form = answerForm.getForm();
        Article article = form.getArticle();
        validateAuthor(userId, article);

        if (answerForm.getReadStatus() == ReadStatus.UNREAD) {
            answerForm.updateReadStatus(ReadStatus.READ);
        }
    }
}
