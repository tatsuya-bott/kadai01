package com.japan.compass.annotation.service.admin.impl;

import com.japan.compass.annotation.domain.ProjectList;
import com.japan.compass.annotation.domain.ProjectModel;
import com.japan.compass.annotation.domain.entity.Project;
import com.japan.compass.annotation.repository.ProjectRepository;
import com.japan.compass.annotation.service.admin.ProjectManagementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectManagementServiceImpl implements ProjectManagementService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectList getProjectList() {
        return new ProjectList(projectRepository.findAll());
    }

    @Override
    public Project getProject(int id) {
        return projectRepository.find(id);
    }

    @Override
    public void createNewProject(ProjectModel projectModel) {
        projectRepository.create(
                Project.builder()
                        .id(0)
                        .name(projectModel.getName())
                        .description(projectModel.getDescription())
                        .type(projectModel.getType())
                        .enabled(projectModel.isEnabled())
                        .build());
    }

    @Override
    public void update(ProjectModel projectModel) {
        projectRepository.update(
                Project.builder()
                        .id(projectModel.getId())
                        .name(projectModel.getName())
                        .description(projectModel.getDescription())
                        .type(projectModel.getType())
                        .enabled(projectModel.isEnabled())
                        .build());
    }

    @Override
    public void delete(int id) {
        projectRepository.delete(id);
    }
}
