package com.japan.compass.annotation.controller;

import com.japan.compass.annotation.repository.ApplicationRepository;
import com.japan.compass.annotation.service.batch.DataBatchService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;

/*
 * local,dev環境でのバッチなどの動作確認用エンドポイント
 */
@Profile({"local", "dev"})
@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class SampleController {

    private final Clock clock;
    private final DataBatchService dataBatchService;
    private final ApplicationRepository applicationRepository;

    @GetMapping("/batch/data/test")
    public String testDataBatch() {
        LocalDateTime today = LocalDateTime.now(clock);
        applicationRepository.updateRecordLastUpdated(today.minusDays(2));
        dataBatchService.runRecordBatch();
        return "batch complete";
    }

}
