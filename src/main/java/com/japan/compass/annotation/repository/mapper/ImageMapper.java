package com.japan.compass.annotation.repository.mapper;

import com.japan.compass.annotation.domain.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

    int count();
    int countByQuestionId(int questionId);
    List<ImageCount> countMap(List<Integer> questionIds);
    List<Integer> findAllEnabledId(int questionId);

    Image find(int id);
    List<Image> findEnabledByIds(List<Integer> ids);
    List<Image> findAll(int limit, int offset);
    List<Image> findAllByQuestionId(int questionId, int limit, int offset);

    void insert(Image image);
    void updateEnabled(int id, boolean enabled);
    void delete(int id);


    @Getter
    @AllArgsConstructor
    class ImageCount {
        private final int questionId;
        private final int count;
    }
}
