package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Project;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ProjectList {

    private final List<Project> list;
}
