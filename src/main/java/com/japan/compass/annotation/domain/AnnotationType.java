package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@AllArgsConstructor
public enum AnnotationType {
    PAIR(0) {
        @Override
        public int numberOfImages() {
            return 2;
        }

        @Override
        public Pair<Integer, Pair<Integer, Integer>> getTargetImageId(List<Integer> imageIds) {

            if (imageIds == null || imageIds.size() < numberOfImages()) {
                return null;
            }
            List<Integer> idList = new ArrayList<>(imageIds);
            Random random = new Random();
            int index1 = random.nextInt(idList.size());
            int value1 = idList.remove(index1);

            int index2 = random.nextInt(idList.size());
            int value2 = idList.remove(index2);

            return Pair.of(null, Pair.of(value1, value2));
        }

        @Override
        public Pair<Image, Pair<Image, Image>> getImagePair(
                Pair<Integer, Pair<Integer, Integer>> idPair,
                List<Image> images
        ) {
            if (idPair == null || idPair.getRight() == null) {
                return null;
            }
            if (images == null || images.size() < numberOfImages()) {
                return null;
            }

            return Pair.of(
                    null,
                    Pair.of(
                        images.stream()
                                .filter(v -> v.getId() == idPair.getRight().getLeft())
                                .findFirst().get(),
                        images.stream()
                                .filter(v -> v.getId() == idPair.getRight().getRight())
                                .findFirst().get()
                    )
            );
        }

        @Override
        public AnnotationImage getAnnotationImage(
                Question question,
                Image baseImage,
                Pair<Image, Image> pairImage
        ) {
            return AnnotationImagePair.builder()
                    .question(question)
                    .pairImage(pairImage)
                    .build();
        }
    },
    BASE_AND_PAIR(1) {
        @Override
        public int numberOfImages() {
            return 3;
        }

        @Override
        public Pair<Integer, Pair<Integer, Integer>> getTargetImageId(List<Integer> imageIds) {

            if (imageIds == null || imageIds.size() < numberOfImages()) {
                return null;
            }
            List<Integer> idList = new ArrayList<>(imageIds);
            Random random = new Random();
            int index1 = random.nextInt(idList.size());
            int value1 = idList.remove(index1);

            int index2 = random.nextInt(idList.size());
            int value2 = idList.remove(index2);
            int index3 = random.nextInt(idList.size());
            int value3 = idList.remove(index3);
            return Pair.of(value1, Pair.of(value2, value3));
        }

        @Override
        public Pair<Image, Pair<Image, Image>> getImagePair(
                Pair<Integer, Pair<Integer, Integer>> idPair,
                List<Image> images
        ) {
            if (idPair == null || idPair.getRight() == null) {
                return null;
            }
            if (images == null || images.size() < numberOfImages()) {
                return null;
            }

            return Pair.of(
                    images.stream()
                            .filter(v -> v.getId() == idPair.getLeft())
                            .findFirst().get(),
                    Pair.of(
                            images.stream()
                                    .filter(v -> v.getId() == idPair.getRight().getLeft())
                                    .findFirst().get(),
                            images.stream()
                                    .filter(v -> v.getId() == idPair.getRight().getRight())
                                    .findFirst().get()
                    )
            );
        }

        @Override
        public AnnotationImage getAnnotationImage(
                Question question,
                Image baseImage,
                Pair<Image, Image> pairImage
        ) {
            return AnnotationImageBasePair.builder()
                    .question(question)
                    .baseImage(baseImage)
                    .pairImage(pairImage)
                    .build();
        }
    },
    ;

    private final int value;

    public static AnnotationType from(int value) {
        for(AnnotationType type: AnnotationType.values()) {
            if(type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

    abstract public int numberOfImages();
    abstract public Pair<Integer, Pair<Integer, Integer>> getTargetImageId(List<Integer> imageIds);
    abstract public Pair<Image, Pair<Image, Image>> getImagePair(
            Pair<Integer, Pair<Integer, Integer>> idPair,
            List<Image> images
    );
    abstract public AnnotationImage getAnnotationImage(
            Question question,
            Image baseImage,
            Pair<Image, Image> pairImage
    );
}
