package com.jason.supplymanagement.dao.Users;

import com.jason.supplymanagement.entity.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    // 自定义方法：根据用户名查询用户
    User findByUsername(String username);

    // 自定义方法：根据角色ID查询用户列表
    List<User> findByRole_RoleId(int roleId);

    boolean existsByEmail(String email);
}