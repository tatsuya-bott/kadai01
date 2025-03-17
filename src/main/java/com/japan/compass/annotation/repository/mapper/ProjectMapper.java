package com.japan.compass.annotation.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.japan.compass.annotation.domain.entity.Project;

@Mapper
public interface ProjectMapper {
    
    Project find(int id);
    List<Project> findAll();
    List<Project> findEnabledAll();
    void disable(int id);
    void insert(Project project);
    void update(Project project);
    void delete(int id);
}
