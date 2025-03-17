package com.japan.compass.annotation.repository;

import java.util.List;

import com.japan.compass.annotation.domain.entity.Project;

public interface ProjectRepository {
    
    Project find(int id);
    List<Project> findAll();
    List<Project> findEnabledAll();
    void disable(int id);
    void create(Project project);
    void update(Project project);
    void delete(int id);
}
