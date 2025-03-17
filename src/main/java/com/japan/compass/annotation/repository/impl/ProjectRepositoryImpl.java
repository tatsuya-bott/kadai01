package com.japan.compass.annotation.repository.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.japan.compass.annotation.domain.entity.Project;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.ProjectRepository;
import com.japan.compass.annotation.repository.mapper.ProjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectMapper projectMapper;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public Project find(int id) {
        try {
            return projectMapper.find(id);
        } catch (TransientDataAccessException e) {
            log.warn("find project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private Project recoverFind(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("find project retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Project> findAll() {
        try {
            return projectMapper.findAll();
        } catch (TransientDataAccessException e) {
            log.warn("find all project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Project> recoverFindAll(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("find all project retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
    
    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Project> findEnabledAll() {
        
        try {
            return projectMapper.findEnabledAll();
        } catch (TransientDataAccessException e) {
            log.warn("find enabled all project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find enabled all project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Project> recoverFindEnabledAll(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("find enabled all project retry exhausted.");
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
           projectMapper.disable(id);
        } catch (TransientDataAccessException e) {
            log.warn("disable project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("disable project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverDisable(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("disable project retry exhausted.");
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
    public void create(Project project) {
        try {
            if (project == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "project is null");
            }

            projectMapper.insert(project);
        } catch (TransientDataAccessException e) {
            log.warn("insert project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("insert project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, Project project) {
        if(e instanceof RetryableException) {
            log.error("insert project retry exhausted.");
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
    public void update(Project project) {
        try {
            if (project == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "project is null");
            }

            projectMapper.update(project);
        } catch (TransientDataAccessException e) {
            log.warn("update project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("update project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdate(RuntimeException e, Project project) {
        if(e instanceof RetryableException) {
            log.error("update project retry exhausted.");
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
           projectMapper.delete(id);
        } catch (TransientDataAccessException e) {
            log.warn("delete project retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("delete project exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverDelete(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("delete project retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
