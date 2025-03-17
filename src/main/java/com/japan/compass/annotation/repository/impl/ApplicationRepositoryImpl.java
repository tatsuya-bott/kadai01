package com.japan.compass.annotation.repository.impl;

import com.japan.compass.annotation.domain.entity.Application;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.ApplicationRepository;
import com.japan.compass.annotation.repository.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final ApplicationMapper applicationMapper;

    @Value("${application.config-name: prod}")
    private String configName;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public Application find() {
        try {
            return applicationMapper.find(configName);
        } catch (TransientDataAccessException e) {
            log.warn("find application retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find application exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private Application recoverFind(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("find application retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void create(Application application) {
        try {
            if (application == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "application is null");
            }

            applicationMapper.insert(configName, application);
        } catch (TransientDataAccessException e) {
            log.warn("create application retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("create application exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, Application application) {
        if(e instanceof RetryableException) {
            log.error("create application retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void updateRecordLastUpdated(LocalDateTime time) {
        try {
            applicationMapper.updateRecordLastUpdated(configName, time);
        } catch (TransientDataAccessException e) {
            log.warn("update application record last updated retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("update application record last updated exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdateRecordLastUpdated(RuntimeException e, LocalDateTime time) {
        if(e instanceof RetryableException) {
            log.error("update application record last updated retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
