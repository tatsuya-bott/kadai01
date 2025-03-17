package com.japan.compass.annotation.service.batch;

import com.japan.compass.annotation.domain.entity.Application;
import com.japan.compass.annotation.domain.entity.Record;
import com.japan.compass.annotation.infrastructure.RecordFileComponent;
import com.japan.compass.annotation.repository.ApplicationRepository;
import com.japan.compass.annotation.repository.RecordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Profile("cron")
@Slf4j
@AllArgsConstructor
@Service
public class DataBatchService {

    private final Clock clock;
    private final ApplicationRepository applicationRepository;
    private final RecordRepository recordRepository;
    private final RecordFileComponent recordFileComponent;

    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Tokyo")
    @Retryable(
            value = Exception.class,
            maxAttemptsExpression = "${batch.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${batch.retry.backoffDelay:60000}"))
    public void runRecordBatch() {
        LocalDateTime today = LocalDateTime.now(clock);
        log.info("run record batch. {}", today);
        // DBから最新の更新日時を取得
        Application application = applicationRepository.find();

        LocalDateTime lastDateTime;
        if (application == null) {
            throw new IllegalStateException("application config is null.");
        }
        lastDateTime = application.getRecordLastUpdated();

        while (lastDateTime.toLocalDate().isBefore(today.toLocalDate())) {
            LocalDate targetDate = lastDateTime.toLocalDate();
            log.info("record batch loop. target date:{}", targetDate);
            List<Record> records = recordRepository.findAll(
                    LocalDateTime.of(targetDate.getYear(), targetDate.getMonth(), targetDate.getDayOfMonth(), 0, 0, 0, 0),
                    LocalDateTime.of(targetDate.getYear(), targetDate.getMonth(), targetDate.getDayOfMonth(), 23, 59, 59, 999)
            );
            log.info("record size: {}", records.size());

            if (CollectionUtils.isEmpty(records)) {
                lastDateTime = lastDateTime.plusDays(1);
                applicationRepository.updateRecordLastUpdated(lastDateTime
                        .withHour(today.getHour())
                        .withMinute(today.getMinute())
                        .withSecond(today.getSecond())
                        .withNano(today.getSecond())
                );
                continue;
            }
            recordFileComponent.createCsv(targetDate);
            recordFileComponent.write(targetDate, records);

            lastDateTime = lastDateTime.plusDays(1);
            applicationRepository.updateRecordLastUpdated(lastDateTime
                    .withHour(today.getHour())
                    .withMinute(today.getMinute())
                    .withSecond(today.getSecond())
                    .withNano(today.getSecond())
            );

            recordFileComponent.createZip(targetDate);
        }
        log.info("end record batch. {}", today);
    }
}
