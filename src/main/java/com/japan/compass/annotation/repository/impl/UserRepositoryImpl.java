package com.japan.compass.annotation.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.japan.compass.annotation.domain.entity.Role;
import com.japan.compass.annotation.domain.entity.User;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.UserRepository;
import com.japan.compass.annotation.repository.mapper.UserMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public int count() {
        try {
            return userMapper.count();
        } catch (TransientDataAccessException e) {
            log.warn("count user retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("count user exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private int recoverCount(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("count user retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public User findById(int id) {
        try {
            return userMapper.findById(id);
        } catch (TransientDataAccessException e) {
            log.warn("find user by id retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find user by id exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private User recoverFindById(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("find user by id retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public User findByMail(String mail) {
        try {
            return userMapper.findByMail(mail);
        } catch (TransientDataAccessException e) {
            log.warn("find user retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find user exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private User recoverFind(RuntimeException e, String mail) {
        if(e instanceof RetryableException) {
            log.error("find user retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<User> findAll(int limit, int offset) {
        try {
            if (limit < 1 || offset < 0) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "limit or offset is invalid");
            }
            return userMapper.findAll(limit, offset);
        } catch (TransientDataAccessException e) {
            log.warn("find all user retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all user exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<User> recoverFindAll(RuntimeException e, int limit, int offset) {
        if(e instanceof RetryableException) {
            log.error("find all user retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void create(String mail, String password, LocalDateTime time, Role ...roles) {
        try {
            userMapper.create(mail, password, time, User.rolesString(roles));
        } catch (TransientDataAccessException e) {
            log.warn("create user retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("create user exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, String mail, String password, LocalDateTime time, Role ...roles) {
        if(e instanceof RetryableException) {
            log.error("create user retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }


    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void updateLastLogined(int id, LocalDateTime lastLogined) {
        try {
            userMapper.updateLastLogined(id, lastLogined);
        } catch (TransientDataAccessException e) {
            log.warn("update user last logined retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("update user last logined exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdateLastLogined(RuntimeException e, int id, LocalDateTime lastLogined) {
        if(e instanceof RetryableException) {
            log.error("update user last logined retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public void updateEnabled(int id, boolean enabled) {
        try {
            userMapper.updateEnabled(id, enabled);
        } catch (TransientDataAccessException e) {
            log.warn("update user enabled retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("update user enabled exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdateEnabled(RuntimeException e, int id, boolean enabled) {
        if(e instanceof RetryableException) {
            log.error("update user enabled retry exhausted.");
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
            userMapper.delete(id);
        } catch (TransientDataAccessException e) {
            log.warn("delete user retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("delete user exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverDelete(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("delete user retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
