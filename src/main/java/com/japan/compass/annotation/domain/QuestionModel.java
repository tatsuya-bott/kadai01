package com.japan.compass.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class QuestionModel {

    @Min(0)
    private int questionId;
    @Min(1)
    private int projectId;
    private String text;
    private boolean enabled;

    @AssertTrue
    private boolean isText() {
        // 全角空白対応のため
        return StringUtils.isNotBlank(this.text);
    }
}
