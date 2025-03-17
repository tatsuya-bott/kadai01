package com.japan.compass.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserModel {
    @Min(1)
    private int id;
    private boolean enabled;
}
