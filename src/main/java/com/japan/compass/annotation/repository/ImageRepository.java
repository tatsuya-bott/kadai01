package com.japan.compass.annotation.repository;

import com.japan.compass.annotation.domain.entity.Image;

import java.util.List;
import java.util.Map;

public interface ImageRepository {

    int count();
    int countByQuestionId(int questionId);
    Map<Integer, Integer> countMap(List<Integer> questionIds);
    List<Integer> findAllEnabledId(int questionId);

    Image find(int id);
    List<Image> findEnabledByIds(List<Integer> ids);
    List<Image> findAll(int limit, int offset);
    List<Image> findAllByQuestionId(int questionId, int limit, int offset);

    void create(Image image);
    void updateEnabled(int id, boolean enabled);
    void delete(int id);
}
