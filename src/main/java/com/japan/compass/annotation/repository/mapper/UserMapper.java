package com.japan.compass.annotation.repository.mapper;

import com.japan.compass.annotation.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {

    int count();
    User findById(int id);
    User findByMail(String mail);
    List<User> findAll(int limit, int offset);

    void create(String mail, String password, LocalDateTime time, String roles);
    void updateLastLogined(int id, LocalDateTime lastLogined);
    void updateEnabled(int id, boolean enabled);
    void delete(int id);
}
