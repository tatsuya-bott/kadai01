package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.repository.mapper.UserMapper;

@Repository
public class UserRepository {
    
    @Autowired
    private UserMapper userMapper;
    
    public User findById(int id) {
        return userMapper.findById(id);
    }

}
