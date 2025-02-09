package com.jason.supplymanagement.dao.Users;

import com.jason.supplymanagement.entity.Users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = {"role.permissions"}) // 确保加载角色和权限信息
    User findByUsername(String username);

    @EntityGraph(attributePaths = {"role.permissions"}) // 确保加载角色和权限信息
    List<User> findByRole_RoleId(int roleId);

    boolean existsByEmail(String email);
}