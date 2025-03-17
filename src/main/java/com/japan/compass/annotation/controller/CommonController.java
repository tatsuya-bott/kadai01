package com.japan.compass.annotation.controller;

import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Slf4j
@Controller
public class CommonController {

    private final ImageFileComponent imageFileComponent;

    @GetMapping("/image/{question_id:\\d+}/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable(value = "question_id") int questionId,
            @PathVariable(value = "filename") String filename
    ) throws ApplicationException {
        Resource file = imageFileComponent.load(String.valueOf(questionId), filename);

        if (!file.exists()) {
            log.error("image resource not found. question_id:{}, filename:{}", questionId, filename);
            throw new ApplicationException(Errors.FILE_NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(file);
    }
}
