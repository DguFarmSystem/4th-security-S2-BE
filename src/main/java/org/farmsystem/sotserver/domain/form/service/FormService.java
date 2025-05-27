package org.farmsystem.sotserver.domain.form.service;

import lombok.RequiredArgsConstructor;
import org.farmsystem.sotserver.domain.article.entity.Article;
import org.farmsystem.sotserver.domain.article.repository.ArticleRepository;
import org.farmsystem.sotserver.domain.form.dto.request.FormCreateRequestDTO;
import org.farmsystem.sotserver.domain.form.dto.response.FormQuestionResponseDTO;
import org.farmsystem.sotserver.domain.form.entity.Form;
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

    // 폼 생성 (질문 생성)
    public void createForm(Long userId, FormCreateRequestDTO formCreateRequest) {
        Article article = articleRepository.findById(formCreateRequest.articleId())
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND));

        User author = article.getAuthor();
        if (!author.getUserId().equals(userId)) {
            throw new ForbiddenException(ARTICLE_AUTHOR_ONLY_FORM_CREATION);
        }

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
}
