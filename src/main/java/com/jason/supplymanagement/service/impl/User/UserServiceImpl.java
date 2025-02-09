package com.jason.supplymanagement.service.impl.User;

import com.jason.supplymanagement.dao.Users.UserDAO;
import com.jason.supplymanagement.dao.Users.RoleDAO;
import com.jason.supplymanagement.entity.Users.Role;
import com.jason.supplymanagement.entity.Users.User;
import com.jason.supplymanagement.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 直接存储明文密码
        user.setPassword(user.getPassword());

        // 设置默认角色ID为0
        Role defaultRole = roleDAO.findById(0).orElse(null);
        if (defaultRole == null) {
            throw new RuntimeException("默认角色不存在");
        }
        user.setRole(defaultRole);

        // 保存用户
        return userDAO.save(user);
    }

    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 返回用户信息
        return user;
    }

    @Override
    public void assignRoleToUser(int userId, int roleId) {
        // 查询用户
        User user = userDAO.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 查询角色
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        // 设置用户角色
        user.setRole(role);
    }

    @Override
    public List<User> listUsersByRole(int roleId) {
        // 查询角色
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        // 查询用户列表
        return userDAO.findByRole_RoleId(roleId);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userDAO.findById(user.getUserId()).orElse(null);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 仅更新传入的非空字段
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }

        userDAO.save(existingUser);
    }
}