package com.japan.compass.annotation.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.japan.compass.annotation.domain.entity.Question;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionMapper {

    Question find(int id);
    List<Question> findAll();
    List<Question> findEnabledAllByProjectId(int id);
    void insert(@Param("question") Question question);
    void update(@Param("question") Question question);
    void disable(int id);
    void delete(int id);
}
