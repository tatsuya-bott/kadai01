package com.japan.compass.annotation.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.domain.support.Limit;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserList {

    public static final Limit LIMIT = Limit.FIFTY;

    private final List<User> list;
    private final int count;

    public List<Integer> getPageList() {
        return IntStream.rangeClosed(1, (count-1)/LIMIT.getValue() + 1)
                .boxed()
                .collect(Collectors.toUnmodifiableList());
    }
}
