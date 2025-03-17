package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.support.Limit;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ImageList {

    public static final Limit LIMIT = Limit.TWO_HUNDRED;

    private final List<Image> list;
    private final int count;

    public List<Integer> getPageList() {
        return IntStream.rangeClosed(1, (count-1)/LIMIT.getValue() + 1)
                .boxed()
                .collect(Collectors.toUnmodifiableList());
    }
}
