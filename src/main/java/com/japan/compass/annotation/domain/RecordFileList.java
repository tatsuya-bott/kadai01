package com.japan.compass.annotation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RecordFileList {

    private final List<String> list;
    private final LocalDateTime lastUpdated;

    public List<String> getDownloadFileList() {
        return this.list;
    }
}
