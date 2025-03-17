package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Question;
import lombok.Builder;
import org.apache.commons.lang3.tuple.Pair;

public class AnnotationImageBasePair extends AnnotationImage {
    private final AnnotationType annotationType = AnnotationType.BASE_AND_PAIR;
    private final Image baseImage;
    private final Pair<Image, Image> pairImage;

    @Builder
    public AnnotationImageBasePair(Question question, Image baseImage, Pair<Image, Image> pairImage) {
        super(question);
        this.baseImage = baseImage;
        this.pairImage = pairImage;
    }

    @Override
    public int numberOfType() {
        return this.annotationType.getValue();
    }

    @Override
    public Image getBaseImage() {
        return this.baseImage;
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
