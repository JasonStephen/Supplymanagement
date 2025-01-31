package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionDAO extends JpaRepository<RolePermission, Integer> {
    // 自定义方法：根据角色ID查询权限列表
    List<RolePermission> findByRole_RoleId(int roleId);

    // 自定义方法：根据权限ID查询角色列表
    List<RolePermission> findByPermission_PermissionId(int permissionId);

    // 自定义方法：根据角色ID和权限ID查询角色权限
    RolePermission findByRole_RoleIdAndPermission_PermissionId(int roleId, int permissionId);
}