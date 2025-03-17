package com.japan.compass.annotation.repository.impl;

import com.japan.compass.annotation.domain.entity.Record;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.RecordRepository;
import com.japan.compass.annotation.repository.mapper.RecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordMapper recordMapper;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Record> findAll(LocalDateTime start, LocalDateTime end) {
        try {
            if (start == null || end == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "start or end is null");
            }

            return recordMapper.findAll(start, end);
        } catch (TransientDataAccessException e) {
            log.warn("find all record retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all record exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Record> recoverFindAll(RuntimeException e, LocalDateTime start, LocalDateTime end) {
        if(e instanceof RetryableException) {
            log.error("find all record retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void create(Record record) {
        try {
            if (record == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "record is null");
            }
            recordMapper.insert(record);
        } catch (TransientDataAccessException e) {
            log.warn("create record retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("create record exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, Record record) {
        if(e instanceof RetryableException) {
            log.error("create record retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
