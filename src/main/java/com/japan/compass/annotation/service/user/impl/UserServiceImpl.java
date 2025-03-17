package com.japan.compass.annotation.service.user.impl;

import com.japan.compass.annotation.domain.AnnotationImage;
import com.japan.compass.annotation.domain.AnnotationModel;
import com.japan.compass.annotation.domain.AnnotationType;
import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Question;
import com.japan.compass.annotation.domain.entity.Record;
import com.japan.compass.annotation.domain.entity.Role;
import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import com.japan.compass.annotation.repository.ImageRepository;
import com.japan.compass.annotation.repository.ProjectRepository;
import com.japan.compass.annotation.repository.QuestionRepository;
import com.japan.compass.annotation.repository.RecordRepository;
import com.japan.compass.annotation.repository.UserRepository;
import com.japan.compass.annotation.service.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final Clock clock;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final QuestionRepository questionRepository;
    private final ImageRepository imageRepository;
    private final RecordRepository recordRepository;
    private final ImageFileComponent imageFileComponent;

    @Override
    public void registerUserInfo(String mail) throws ApplicationException {
        User user = userRepository.findByMail(mail);

        if (user == null) {
            userRepository.create(mail, User.USER_PASSWORD, LocalDateTime.now(clock), Role.USER);
            return;
        }

        if (user.isEnabled()) {
            userRepository.updateLastLogined(user.getId(), LocalDateTime.now(clock));
            return;
        } else {
            throw new ApplicationException(Errors.LOGIN_DISABLE_ERROR);
        }
    }

    @Override
    public ProjectList getEnabledProjects() {
        return new ProjectList(projectRepository.findEnabledAll());
    }

    @Override
    public QuestionList getEnabledQuestionsByProjectId(int projectId) {
        List<Question> questions = questionRepository.findEnabledAllByProjectId(projectId);
        if (questions.size() == 0) {
            return new QuestionList(List.of());
        }
        Map<Integer, Integer> countMap = imageRepository.countMap(questions.stream()
                .map(Question::getId)
                .collect(Collectors.toUnmodifiableList()));

        List<Question> displayQuestions = questions.stream().filter(question -> {
            Integer count = countMap.get(question.getId());
            AnnotationType annotationType = AnnotationType.from(question.getProject().getType());
            // projectテーブルのtypeに想定外の値が入っている場合。直接DBを操作しない限り起こり得ない想定
            if (annotationType == null) {
                throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "annotation type is invalid.");
            }

            return count != null && annotationType.numberOfImages() <= count;
        }) .collect(Collectors.toList());

        return new QuestionList(displayQuestions.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> {
                            Collections.shuffle(l);
                            return l;
                        }
                        )));
    }

    @Override
    public AnnotationImage getAnnotationImage(int questionId) throws ApplicationException {
        Question question = questionRepository.find(questionId);
        // 不正リクエストもしくは状態がおかしい場合
        if (question == null) {
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        AnnotationType annotationType = AnnotationType.from(question.getProject().getType());
        // projectテーブルのtypeに想定外の値が入っている場合。直接DBを操作しない限り起こり得ない想定
        if (annotationType == null) {
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "annotation type is invalid.");
        }

        List<Integer> imageIds = imageRepository.findAllEnabledId(questionId);
        // 画像枚数の条件は質問文表示で制御しているので、不正なリクエストまたは管理側での意図的な操作でしか起こり得ない
        if (imageIds == null || imageIds.size() < annotationType.numberOfImages()) {
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "number of image ids is not.");
        }

        Pair<Integer, Pair<Integer, Integer>> targetImageId = annotationType.getTargetImageId(imageIds);
        if (targetImageId == null || targetImageId.getRight() == null) {
            throw new SystemException(Errors.INTERNAL_SERVER_ERROR, "number of image ids is not.");
        }
        List<Integer> targetList = Stream.of(
                targetImageId.getLeft(),
                targetImageId.getRight().getLeft(),
                targetImageId.getRight().getRight()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

        List<Image> images = imageRepository.findEnabledByIds(targetList);
        if (images == null || images.size() != annotationType.numberOfImages()) {
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "number of images is not.");
        }

        images.stream().forEach(v -> {
            if (!imageFileComponent.exist(String.valueOf(questionId), v.getFilePath())) {
                throw new SystemException(Errors.ILLEGAL_STATE_ERROR, "image file not exist.");
            }
        });

        Pair<Image, Pair<Image, Image>> pairImage = annotationType.getImagePair(targetImageId, images);
        if (pairImage == null || pairImage.getRight() == null) {
            throw new SystemException(Errors.INTERNAL_SERVER_ERROR, "number of image ids is not.");
        }
        return annotationType.getAnnotationImage(
                question,
                pairImage.getLeft(),
                pairImage.getRight()
        );
    }

    @Override
    public void registerAnnotation(User user, AnnotationModel annotationModel) {
        Record record = Record.builder()
                .userId(user.getId())
                .projectId(annotationModel.getProjectId())
                .displayImageIds(annotationModel.getDisplayImageIds())
                .questionId(annotationModel.getQuestionId())
                .answerImageStatuses(annotationModel.getAnswerImageStatuses())
                .answerStartTime(annotationModel.getAnswerStartTime())
                .answerEndTime(annotationModel.getAnswerEndTime())
                .created(LocalDateTime.now(clock))
                .build();

        recordRepository.create(record);
    }
}
