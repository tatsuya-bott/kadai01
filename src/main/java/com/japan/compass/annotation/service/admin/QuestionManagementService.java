package com.japan.compass.annotation.service.admin;

import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.QuestionModel;
import com.japan.compass.annotation.domain.entity.Question;

public interface QuestionManagementService {

    QuestionList getQuestionList();
    Question getQuestion(int id);
    void createNewQuestion(QuestionModel questionModel);
    void update(QuestionModel questionModel);
    void delete(int id);
}
