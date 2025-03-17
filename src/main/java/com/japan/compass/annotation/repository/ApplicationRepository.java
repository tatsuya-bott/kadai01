package com.japan.compass.annotation.repository;

import com.japan.compass.annotation.domain.entity.Application;

import java.time.LocalDateTime;

public interface ApplicationRepository {

    Application find();

    void create(Application application);
    void updateRecordLastUpdated(LocalDateTime time);
}
