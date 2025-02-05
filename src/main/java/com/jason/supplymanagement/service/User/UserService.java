package com.jason.supplymanagement.service.User;

import com.jason.supplymanagement.entity.Users.User;

import java.util.List;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-21 13:58:26
 * Github: https://github.com/JasonStephen
 */

public interface UserService {
    // 注册用户
    User register(User user);

    // 用户登录
    User login(String username, String password);

    // 为用户分配角色
    void assignRoleToUser(int userId, int roleId);

    //查询某角色下的所有用户列表
    List<User> listUsersByRole(int roleId);


}
