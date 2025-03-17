package com.japan.compass.annotation.service.user;

import com.japan.compass.annotation.domain.AnnotationImage;
import com.japan.compass.annotation.domain.AnnotationModel;
import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.exception.ApplicationException;

public interface UserService {

    void registerUserInfo(String mail) throws ApplicationException;

    ProjectList getEnabledProjects();
    QuestionList getEnabledQuestionsByProjectId(int projectId);
    AnnotationImage getAnnotationImage(int questionId) throws ApplicationException;
    void registerAnnotation(User user, AnnotationModel annotationModel);
}
