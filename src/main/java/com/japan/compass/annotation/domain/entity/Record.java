package com.japan.compass.annotation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    private long id;
    private int userId;
    private int projectId;
    @NonNull
    private String displayImageIds;
    private int questionId;
    private String answerImageStatuses;
    private String answerStartTime;
    private String answerEndTime;
    @NonNull
    private LocalDateTime created;

    public String toCsvString() {
        return this.projectId
                + "," + this.userId
                + "," +  this.displayImageIds
                + "," +  this.questionId
                + "," +  this.answerImageStatuses
                + "," +  this.answerStartTime
                + "," +  this.answerEndTime;
    }
}
