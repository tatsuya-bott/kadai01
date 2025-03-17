package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Question;
import lombok.Builder;
import org.apache.commons.lang3.tuple.Pair;

public class AnnotationImagePair extends AnnotationImage {
    private final AnnotationType annotationType = AnnotationType.PAIR;
    private final Pair<Image, Image> pairImage;

    @Builder
    public AnnotationImagePair(Question question, Pair<Image, Image> pairImage) {
        super(question);
        this.pairImage = pairImage;
    }

    @Override
    public int numberOfType() {
        return this.annotationType.getValue();
    }

    @Override
    public Image getBaseImage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image getLeftImage() {
        return this.pairImage.getLeft();
    }

    @Override
    public Image getRightImage() {
        return this.pairImage.getRight();
    }
}
