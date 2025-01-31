package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Integer> {
    // 自定义方法：根据角色名称查询角色
    Role findByRoleName(String roleName);
}