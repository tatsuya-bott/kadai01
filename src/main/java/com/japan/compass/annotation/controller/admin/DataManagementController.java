package com.japan.compass.annotation.controller.admin;

import com.japan.compass.annotation.domain.RecordFileList;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.service.admin.DataManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Slf4j
@Validated
@Controller
@RequestMapping("/admin/record")
public class DataManagementController {

    private final DataManagementService dataManagementService;

    /*
     * データ管理画面
     */
    @GetMapping("")
    public String getRecord(
            Model model
    ) {
        RecordFileList recordFileList = dataManagementService.getRecordFileList();
        model.addAttribute("recordFileList", recordFileList);
        return "admin/record";
    }

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadRecord(
            @Pattern(regexp = "20[0-9]{2}_[0-1][1-9]_[0-9]+\\.zip") @RequestParam(value = "filename", required = true) String filename
    ) throws ApplicationException {
        Resource resource = dataManagementService.loadZip(filename);

        if (!resource.exists()) {
            log.error("record resource not found. filename:{}", filename);
            throw new ApplicationException(Errors.FILE_NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }
}
