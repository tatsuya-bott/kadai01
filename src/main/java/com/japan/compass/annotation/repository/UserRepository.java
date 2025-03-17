package com.japan.compass.annotation.repository;

import com.japan.compass.annotation.domain.entity.Role;
import com.japan.compass.annotation.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository {

    int count();
    User findById(int id);
    User findByMail(String mail);
    List<User> findAll(int limit, int offset);

    void create(String mail, String password, LocalDateTime time, Role ...roles);
    void updateLastLogined(int id, LocalDateTime lastLogined);
    void updateEnabled(int id, boolean enabled);
    void delete(int id);
}
