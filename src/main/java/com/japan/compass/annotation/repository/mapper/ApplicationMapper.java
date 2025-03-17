package com.japan.compass.annotation.repository.mapper;

import com.japan.compass.annotation.domain.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface ApplicationMapper {

    Application find(String name);

    void insert(String name, @Param("application") Application application);
    void updateRecordLastUpdated(String name, LocalDateTime time);
}
