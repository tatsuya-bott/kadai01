package com.japan.compass.annotation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private int type;
    private boolean enabled;
}
