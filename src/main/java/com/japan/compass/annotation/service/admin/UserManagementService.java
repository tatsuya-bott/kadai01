package com.japan.compass.annotation.service.admin;

import com.japan.compass.annotation.domain.UserList;
import com.japan.compass.annotation.domain.UserModel;
import com.japan.compass.annotation.domain.entity.User;

public interface UserManagementService {

    UserList getUserList(int limit, int offset);

    User getUser(int id);
    void updateUser(UserModel userModel);
    void deleteUser(int id);
}
