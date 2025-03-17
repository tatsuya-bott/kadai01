package com.japan.compass.annotation.infrastructure;

import com.japan.compass.annotation.domain.entity.Record;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.util.List;

public interface RecordFileComponent {

    void init();

    Resource loadZip(String filename);
    void createCsv(LocalDate localDate);
    void write(LocalDate localDate, List<Record> records);
    void createZip(LocalDate localDate);
    List<String> listZip();
}
