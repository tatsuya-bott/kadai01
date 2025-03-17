package com.japan.compass.annotation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private int id;
    @NonNull
    private Question question;
    @NonNull
    private String filePath;
    @NonNull
    private LocalDateTime created;
    private boolean enabled;
}
