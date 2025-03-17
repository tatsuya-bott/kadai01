package com.japan.compass.annotation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Application {

    private String name;
    private LocalDateTime recordLastUpdated;
}
