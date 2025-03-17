package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract public class AnnotationImage {

    private final Question question;

    abstract public int numberOfType();
    abstract public Image getBaseImage();
    abstract public Image getLeftImage();
    abstract public Image getRightImage();
}
