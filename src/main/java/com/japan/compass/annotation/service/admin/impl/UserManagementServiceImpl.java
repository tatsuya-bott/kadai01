package com.japan.compass.annotation.service.admin.impl;

import com.japan.compass.annotation.domain.UserModel;
import com.japan.compass.annotation.domain.entity.User;
import org.springframework.stereotype.Service;

import com.japan.compass.annotation.domain.UserList;
import com.japan.compass.annotation.repository.UserRepository;
import com.japan.compass.annotation.service.admin.UserManagementService;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Service
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserList getUserList(int limit, int offset) {
        int userCount = userRepository.count();
        List<User> users = userRepository.findAll(limit, offset);
        return new UserList(users, userCount);
    }

    @Override
    public User getUser(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(UserModel userModel) {
        userRepository.updateEnabled(userModel.getId(), userModel.isEnabled());
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
