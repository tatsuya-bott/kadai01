package com.japan.compass.annotation.service.admin;

import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.ProjectModel;
import com.japan.compass.annotation.domain.entity.Project;

public interface ProjectManagementService {

    ProjectList getProjectList();
    Project getProject(int id);
    void createNewProject(ProjectModel projectModel);
    void update(ProjectModel projectModel);
    void delete(int id);
}
