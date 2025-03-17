package com.japan.compass.annotation.domain.entity;

import java.time.LocalDateTime;

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
public class Question {
    private int id;
    @NonNull
    private Project project;
    @NonNull
    private String text;
    @NonNull
    private LocalDateTime created;
    private boolean enabled;
}
