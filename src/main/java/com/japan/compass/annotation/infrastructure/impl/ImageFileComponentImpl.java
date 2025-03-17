package com.japan.compass.annotation.infrastructure.impl;

import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageFileComponentImpl implements ImageFileComponent {

    private final ResourceLoader resourceLoader;

    @Value("${image.base-url:./image/}")
    private String baseUrl;

    @Override
    public void init() {
        Path rootPath = Path.of(baseUrl);
        if (!Files.exists(rootPath)) {
            log.info("root dir not exist");
            try {
                Files.createDirectory(rootPath);
            } catch (IOException e) {
                throw new SystemException(Errors.FILE_IO_ERROR, e);
            }
        }
    }

    @Override
    public Resource load(String dir, String filename) {
        return resourceLoader.getResource(Path.of(baseUrl + dir + "/" + filename).toUri().toString());
    }

    @Override
    public void put(InputStream inputStream, String dir, String filename) {
        try {
            Files.copy(inputStream, Path.of(baseUrl + dir + "/" + filename));
        } catch (FileAlreadyExistsException e) {
            log.error("file already exist exception.", e);
            throw new SystemException(Errors.FILE_ALREADY_EXIST_ERROR, e);
        } catch (IOException e) {
            log.warn("file io exception.", e);
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
    }

    @Override
    public boolean exist(String dir, String filename) {
        return Files.exists(Path.of(baseUrl + dir + "/" + filename));
    }

    @Override
    public void createDirectories(List<Integer> list) {
        list.stream()
                .filter(Objects::nonNull)
                .forEach(v -> {
                    Path targetPath = Path.of(baseUrl + v);

                    if (!Files.exists(targetPath)) {
                        try {
                            Files.createDirectory(targetPath);
                        } catch (IOException e) {
                            throw new SystemException(Errors.FILE_IO_ERROR, e);
                        }
                    }
                });
    }

    @Override
    public void delete(String dir, String filename) {
        try {
            boolean deleted = Files.deleteIfExists(Path.of(baseUrl + dir + "/" + filename));
            if (!deleted) {
                log.warn("image file not exist. {}", baseUrl + dir + "/" + filename);
            }
        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
    }

    @Override
    public void deleteDirectory(String dir) {
        try {
            if (!Files.exists(Path.of(baseUrl + dir))) {
                log.warn("image directory not exist. {}", baseUrl + dir);
                return;
            }
            FileUtils.deleteDirectory(Path.of(baseUrl + dir).toFile());
        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
    }
}
