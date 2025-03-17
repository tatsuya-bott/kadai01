package com.japan.compass.annotation.infrastructure.impl;

import com.japan.compass.annotation.domain.entity.Record;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.infrastructure.RecordFileComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecordFileComponentImpl implements RecordFileComponent {

    private final ResourceLoader resourceLoader;

    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM");
    private static final int LOOP_LIMIT = 300;

    @Value("${record.base-url:./record/}")
    private String baseUrl;

    @Value("${record.limit-byte:1000000000}")
    private long limitSize;

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
        Path zipRootPath = Path.of(baseUrl + "zip");
        if (!Files.exists(zipRootPath)) {
            log.info("zip dir not exist");
            try {
                Files.createDirectory(zipRootPath);
            } catch (IOException e) {
                throw new SystemException(Errors.FILE_IO_ERROR, e);
            }
        }
    }

    @Override
    public Resource loadZip(String filename) {
        return resourceLoader.getResource(Path.of(baseUrl + "zip/" + filename).toUri().toString());
    }

    @Override
    public void createCsv(LocalDate localDate) {
        try {
            String defaultFilename = localDate.format(FILE_FORMATTER) + "_" + 1 + ".csv";
            if (!exist(defaultFilename)) {
                createCsvFile(defaultFilename);
                log.info("create new csv file. {}", defaultFilename);
                return;
            }

            for (int i=1; i<LOOP_LIMIT ;i++) {
                String filename = localDate.format(FILE_FORMATTER) + "_" + i + ".csv";
                if (!exist(filename)) {
                    log.error("csv file not found. {}", localDate);
                    throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
                }

                if (!isOverLimit(filename)) {
                    return;
                }

                log.info("file is over limit. {}", filename);
                String nextFileName = localDate.format(FILE_FORMATTER) + "_" + (i + 1) + ".csv";
                if (!exist(nextFileName)) {
                    createCsvFile(nextFileName);
                    log.info("create next csv file. {}", nextFileName);
                    return;
                }
            }
        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
        log.error("create csv not found error. {}", localDate);
        throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
    }

    @Override
    public void write(LocalDate localDate, List<Record> records) {
        for (int i=1; i<LOOP_LIMIT ;i++) {
            String filename = localDate.format(FILE_FORMATTER) + "_" + i + ".csv";
            String nextFileName = localDate.format(FILE_FORMATTER) + "_" + (i + 1) + ".csv";
            if (!exist(filename)) {
                log.error("csv file not found. {}", localDate);
                throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
            }

            if (exist(nextFileName)) {
                continue;
            }

            try {
                if (isOverLimit(filename)) {
                    log.error("limit is over error. {}", filename);
                    throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
                }

                String backupFile = filename + ".bak";
                Files.copy(Path.of(baseUrl +  filename), Path.of(baseUrl +  backupFile));
                appendRecord(backupFile, records);
                Files.move(Path.of(baseUrl +  backupFile), Path.of(baseUrl +  filename), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new SystemException(Errors.FILE_IO_ERROR, e);
            }
            return;
        }
        log.error("csv file not found. {}", localDate);
        throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
    }

    @Override
    public void createZip(LocalDate localDate) {
        if (!Files.isDirectory(Path.of(baseUrl + "zip"))) {
            log.error("zip directory not found. {}", localDate);
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
        }

        if (!exist(localDate.format(FILE_FORMATTER) + "_1.csv")) {
            log.error("first csv file not found. {}", localDate.format(FILE_FORMATTER) + "_1.csv");
            throw new SystemException(Errors.ILLEGAL_STATE_ERROR);
        }

        for(int i=1; i<LOOP_LIMIT; i++) {
            String targetFile = localDate.format(FILE_FORMATTER) + "_" + i + ".csv";
            if (!exist(targetFile)) {
                break;
            }

            String zipFileName = localDate.format(FILE_FORMATTER) + "_" + i + ".zip";

            try (FileOutputStream fileOutputStream = new FileOutputStream(Path.of(baseUrl + "zip/" +  zipFileName).toFile());
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                 ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream)) {

                byte[] data = Files.readAllBytes(Path.of(baseUrl + targetFile));
                ZipEntry zipEntry = new ZipEntry(targetFile);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(data);
                zipOutputStream.closeEntry();
                log.info("created zip. {}", zipFileName);
            } catch (IOException e) {
                throw new SystemException(Errors.FILE_IO_ERROR, e);
            }
        }
    }

    @Override
    public List<String> listZip() {
        try {
            return Files.list(Path.of(baseUrl + "zip"))
                    .map(p -> p.toFile().getName())
                    .filter(v -> v.endsWith(".zip"))
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
    }

    private void createCsvFile(String filename) throws IOException {
        Path filePath = Path.of(baseUrl +  filename);
        Files.createFile(filePath);
        Files.write(filePath, List.of("プロジェクトID,ユーザーID,表示画像ID,質問文ID,回答結果,回答画面表示時刻,回答終了時刻"), StandardCharsets.UTF_8);
    }

    private boolean exist(String filename) {
        return Files.exists(Path.of(baseUrl +  filename));
    }

    private boolean isOverLimit(String filename) throws IOException {
         return limitSize < Files.size(Path.of(baseUrl +  filename));
    }

    private void appendRecord(String filename, List<Record> records) throws IOException {
        List<String> listString = records.stream()
                .map(Record::toCsvString)
                .collect(Collectors.toUnmodifiableList());
        Files.write(Path.of(baseUrl +  filename), listString, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }
}
