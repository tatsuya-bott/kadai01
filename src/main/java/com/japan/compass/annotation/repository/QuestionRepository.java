package com.japan.compass.annotation.repository;

import java.util.List;

import com.japan.compass.annotation.domain.entity.Question;

public interface QuestionRepository {

    Question find(int id);
    List<Question> findAll();
    List<Question> findEnabledAllByProjectId(int id);
    void create(Question question);
    void update(Question question);
    void disable(int id);
    void delete(int id);
}
