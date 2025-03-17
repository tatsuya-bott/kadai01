package com.japan.compass.annotation.service.admin.impl;

import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.QuestionModel;
import com.japan.compass.annotation.domain.entity.Project;
import com.japan.compass.annotation.domain.entity.Question;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import com.japan.compass.annotation.repository.QuestionRepository;
import com.japan.compass.annotation.service.admin.QuestionManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class QuestionManagementServiceImpl implements QuestionManagementService {

    private final Clock clock;
    private final QuestionRepository questionRepository;
    private final ImageFileComponent imageFileComponent;

    @Override
    public QuestionList getQuestionList() {
        return new QuestionList(questionRepository.findAll());
    }

    @Override
    public Question getQuestion(int id) {
        return questionRepository.find(id);
    }

    @Transactional
    @Override
    public void createNewQuestion(QuestionModel questionModel) {
        questionRepository.create(
                Question.builder()
                        // projectIdのみ更新に利用
                        .project(Project.builder()
                                .id(questionModel.getProjectId())
                                .name("")
                                .description("")
                                .build())
                        .text(questionModel.getText())
                        .created(LocalDateTime.now(clock))
                        .enabled(questionModel.isEnabled())
                        .build());

        List<Question> questions = questionRepository.findAll();
        if (CollectionUtils.isEmpty(questions)) {
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "question is empty.");
        }
        imageFileComponent.createDirectories(questions.stream()
                .map(Question::getId)
                .collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public void update(QuestionModel questionModel) {
        questionRepository.update(
                Question.builder()
                        .id(questionModel.getQuestionId())
                        // projectIdのみ更新に利用
                        .project(Project.builder()
                                .id(questionModel.getProjectId())
                                .name("")
                                .description("")
                                .build())
                        .text(questionModel.getText())
                        .created(LocalDateTime.now(clock))
                        .enabled(questionModel.isEnabled())
                        .build()
        );
    }

    @Transactional
    @Override
    public void delete(int id) {
        questionRepository.delete(id);
        imageFileComponent.deleteDirectory(String.valueOf(id));
    }
}
