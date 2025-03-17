package com.japan.compass.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class AnnotationModel {
    @Min(1)
    private int projectId;
    @Min(1)
    private int questionId;
    private int answerImageId;
    private boolean isNeutral;
    @NotNull
    @Pattern(regexp = "[0-9]{13}")
    private String answerStartTime;
    @NotNull
    @Pattern(regexp = "[0-9]{13}")
    private String answerEndTime;
    private String displayImageIds;
    @Min(0) @Max(1)
    private int type;

    public String getAnswerImageStatuses() {
        if (isNeutral) {
            return Arrays.stream(this.displayImageIds.split(","))
                    .map(v -> "0")
                    .collect(Collectors.joining(","));
        }

        return Arrays.stream(this.displayImageIds.split(","))
                .map(v -> v.equals(String.valueOf(answerImageId)) ? "1": "0")
                .collect(Collectors.joining(","));
    }

    @AssertTrue(message = "displayImageIds")
    private boolean isValidDisplayImageIds() {
        if (StringUtils.isBlank(this.displayImageIds)) {
            return false;
        }

        String[] values = this.displayImageIds.split(",");
        if (!(type == 0 && values.length == 2 || type == 1 && values.length == 3)) {
            return false;
        }

        if (Arrays.stream(values)
                .map(v -> NumberUtils.toInt(v, 0))
                .anyMatch(v -> v < 1)) {
            return false;
        }

        return true;
    }

    @AssertTrue(message = "answerImageId")
    private boolean isValidAnswerImageId() {
        if (StringUtils.isBlank(this.displayImageIds)) {
            return false;
        }

        if (this.isNeutral && this.answerImageId == 0) {
            return true;
        }

        if (this.answerImageId < 1) {
            return false;
        }

        if (!Arrays.asList(this.displayImageIds.split(",")).contains(String.valueOf(this.answerImageId))) {
            return false;
        }

        if (type == 1 && this.displayImageIds.split(",")[0].equals(String.valueOf(this.answerImageId))) {
            return false;
        }

        return true;
    }
}
