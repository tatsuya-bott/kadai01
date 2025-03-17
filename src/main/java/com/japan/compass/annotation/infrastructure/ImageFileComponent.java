package com.japan.compass.annotation.infrastructure;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface ImageFileComponent {

    void init();

    Resource load(String dir, String filename);
    void put(InputStream inputStream, String dir, String filename);
    boolean exist(String dir, String filename);
    void createDirectories(List<Integer> list);
    void delete(String dir, String filename);
    void deleteDirectory(String dir);
}
