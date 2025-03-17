package com.japan.compass.annotation.repository;

import com.japan.compass.annotation.domain.entity.Record;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository {

    List<Record> findAll(LocalDateTime start, LocalDateTime end);
    void create(Record record);
}
