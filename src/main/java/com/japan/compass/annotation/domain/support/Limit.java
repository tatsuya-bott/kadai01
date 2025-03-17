package com.japan.compass.annotation.domain.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Limit {
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    ;

    private final int value;
}
