package com.jason.supplymanagement.dao.Users;

import com.jason.supplymanagement.entity.Users.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDAO extends JpaRepository<Permission, Integer> {
    // 自定义方法：根据权限名称查询权限
    Permission findByPermissionName(String permissionName);

    // 自定义方法：根据权限代码查询权限
    Permission findByPermissionCode(String permissionCode);
}