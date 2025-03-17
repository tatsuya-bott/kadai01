package com.japan.compass.annotation.domain;

import com.japan.compass.annotation.domain.entity.Question;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class QuestionList {

    private final List<Question> list;

    public QuestionList getTargetList(int project_id) {
        return new QuestionList(this.list.stream()
                .filter(question -> question.getProject().getId() == project_id)
                .collect(Collectors.toUnmodifiableList()));
    }
}
