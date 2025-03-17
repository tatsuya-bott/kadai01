package com.japan.compass.annotation.repository.impl;

import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.repository.ImageRepository;
import com.japan.compass.annotation.repository.mapper.ImageMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageMapper imageMapper;

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public int count() {
        try {
            return imageMapper.count();
        } catch (TransientDataAccessException e) {
            log.warn("count image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("count image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private int recoverCount(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("count image retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public int countByQuestionId(int questionId) {
        try {
            return imageMapper.countByQuestionId(questionId);
        } catch (TransientDataAccessException e) {
            log.warn("count image by question id retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("count image by question id exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private int recoverCountByQuestionId(RuntimeException e, int questionId) {
        if(e instanceof RetryableException) {
            log.error("count image by question id retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }


    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public Map<Integer, Integer> countMap(List<Integer> questionIds) {
        try {
            if (CollectionUtils.isEmpty(questionIds)) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "ids is empty.");
            }

            Map<Integer, Integer> resultMap = new HashMap<>();
            imageMapper.countMap(questionIds)
                    .forEach(v -> resultMap.put(v.getQuestionId(), v.getCount()));
            return resultMap;
        } catch (TransientDataAccessException e) {
            log.warn("image count map retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("image count map exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private Map<Integer, Integer> recoverCountMap(RuntimeException e, List<Integer> questionIds) {
        if(e instanceof RetryableException) {
            log.error("image count map retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Integer> findAllEnabledId(int questionId) {
        try {
            return imageMapper.findAllEnabledId(questionId);
        } catch (TransientDataAccessException e) {
            log.warn("find all image id retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all image id exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Integer> recoverFindAllEnabledId(RuntimeException e, int questionId) {
        if(e instanceof RetryableException) {
            log.error("find all image id retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public Image find(int id) {
        try {
            return imageMapper.find(id);
        } catch (TransientDataAccessException e) {
            log.warn("find image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private Image recoverFind(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("find image retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Image> findEnabledByIds(List<Integer> ids) {
        try {
            if (CollectionUtils.isEmpty(ids)) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "ids is empty.");
            }

            List<Integer> targetList = ids.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableList());

            if (targetList.size() == 0) {
                log.warn("target image id list is empty");
                return List.of();
            }

            return imageMapper.findEnabledByIds(targetList);
        } catch (TransientDataAccessException e) {
            log.warn("find image by ids retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find image by ids exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Image> recoverFindByEnabledIds(RuntimeException e, List<Integer> ids) {
        if(e instanceof RetryableException) {
            log.error("find image by ids retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Image> findAll(int limit, int offset) {
        try {
            if (limit < 1 || offset < 0) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "limit or offset is invalid");
            }
            return imageMapper.findAll(limit, offset);
        } catch (TransientDataAccessException e) {
            log.warn("find all image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Image> recoverFindAll(RuntimeException e, int limit, int offset) {
        if(e instanceof RetryableException) {
            log.error("find all image retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }

    @Retryable(
            value = RetryableException.class,
            maxAttemptsExpression = "${db.retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${db.retry.backoffDelay:500}"))
    @Override
    public List<Image> findAllByQuestionId(int questionId, int limit, int offset) {
        try {
            if (limit < 1 || offset < 0) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "limit or offset is invalid");
            }
            return imageMapper.findAllByQuestionId(questionId, limit, offset);
        } catch (TransientDataAccessException e) {
            log.warn("find all image by question id retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find all image by question id exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private List<Image> recoverFindAllByQuestionId(RuntimeException e, int questionId, int limit, int offset) {
        if(e instanceof RetryableException) {
            log.error("find all image by question id retry exhausted.");
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
    public void create(Image image) {
        try {
            if (image == null) {
                throw new SystemException(Errors.ILLEGAL_ARGUMENT_ERROR, "image is null");
            }

            imageMapper.insert(image);
        } catch (TransientDataAccessException e) {
            log.warn("create image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("create image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverCreate(RuntimeException e, Image image) {
        if(e instanceof RetryableException) {
            log.error("create image retry exhausted.");
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
            imageMapper.updateEnabled(id, enabled);
        } catch (TransientDataAccessException e) {
            log.warn("update image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("update image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverUpdateEnabled(RuntimeException e, int id, boolean enabled) {
        if(e instanceof RetryableException) {
            log.error("update image retry exhausted.");
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
            imageMapper.delete(id);
        } catch (TransientDataAccessException e) {
            log.warn("delete image retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("delete image exception. " + e);
            throw new SystemException(Errors.DB_ERROR, e);
        }
    }

    @Recover
    private void recoverDelete(RuntimeException e, int id) {
        if(e instanceof RetryableException) {
            log.error("delete image retry exhausted.");
            throw new SystemException(Errors.DB_ERROR, e.getCause());
        }
        throw e;
    }
}
