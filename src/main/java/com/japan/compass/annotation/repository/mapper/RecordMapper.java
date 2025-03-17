package com.japan.compass.annotation.repository.mapper;

import com.japan.compass.annotation.domain.entity.Record;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RecordMapper {

    List<Record> findAll(LocalDateTime start, LocalDateTime end);
    void insert(Record record);
}
