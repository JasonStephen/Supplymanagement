package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.Permission;

import java.util.List;

public interface PermissionService {
    // 新增权限项
    Permission addPermission(Permission permission);

    // 查询权限项请（包括关联角色数）
    Permission getPermission(int permissionId);

    // 查询所有权限
    List<Permission> getAllPermissions();

    // 更新权限信息
    void updatePermission(Permission permission);

    // 删除权限项
    void deletePermission(int permissionId);

    // 查询权限项
    Permission findByPermissionName(String permissionName);




}