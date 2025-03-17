package com.japan.compass.annotation.repository.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.japan.compass.annotation.domain.entity.Question;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.QuestionRepository;
import com.japan.compass.annotation.repository.mapper.QuestionMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    
    private final QuestionMapper questionMapper;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public Question find(int id) {
        try {
            return questionMapper.find(id);
        } catch (TransientDataAccessException e) {
            log.warn("find question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private Question recoverFind(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("find question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Question> findAll() {
        try {
            return questionMapper.findAll();
        } catch (TransientDataAccessException e) {
            log.warn("find all question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Question> recoverFindAll(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("find all question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Question> findEnabledAllByProjectId(int id){
        try {
            return questionMapper.findEnabledAllByProjectId(id);
        } catch (TransientDataAccessException e) {
            log.warn("find enabled all by projectId retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find enabled all by projectId exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Question> recoverFindEnabledAllByProjectId(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("find enabled all by projectId retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            recover = "recoverCreate",
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void create(Question question) {
        try {
            if (question == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "question is null");
            }

            questionMapper.insert(question);
        } catch (TransientDataAccessException e) {
            log.warn("create question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("create question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, Question question) {
        if(e instanceof RetryableException) {
            log.error("create question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            recover = "recoverUpdate",
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void update(Question question) {
        try {
            if (question == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "question is null");
            }

            questionMapper.update(question);
        } catch (TransientDataAccessException e) {
            log.warn("insert question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("insert question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdate(RuntimeException e, Question question) {
        if(e instanceof RetryableException) {
            log.error("update question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
    
    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void disable(int id) {
        try {
            questionMapper.disable(id);
        } catch (TransientDataAccessException e) {
            log.warn("disable question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("disable question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }
    
    @Recover
    private void recoverDisable(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("disable question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
    
    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void delete(int id) {
        try {
            questionMapper.delete(id);
        } catch (TransientDataAccessException e) {
            log.warn("delete question retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("delete question exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }
    
    @Recover
    private void recoverDelete(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("delete question retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
