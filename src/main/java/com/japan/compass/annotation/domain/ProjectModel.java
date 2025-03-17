package com.japan.compass.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ProjectModel {

    @Min(0)
    private int id;
    private String name;
    @NotNull
    private String description;
    @Min(0) @Max(1)
    private int type;
    private boolean enabled;

    @AssertTrue
    private boolean isName() {
        // 全角空白対応のため
        return StringUtils.isNotBlank(this.name);
    }

    public String getDescription() {
        return StringUtils.strip(description);
    }
}
