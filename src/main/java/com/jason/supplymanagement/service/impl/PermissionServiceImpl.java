package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.entity.Permission;
import com.jason.supplymanagement.dao.PermissionDAO;
import com.jason.supplymanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;

    @Override
    public Permission addPermission(Permission permission) {
        return permissionDAO.save(permission);
    }

    @Override
    public Permission getPermission(int permissionId) {
        Permission permission = permissionDAO.findById(permissionId).orElse(null);
        if (permission == null) {
            throw new RuntimeException("权限项不存在");
        }
        return permission;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionDAO.findAll();
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionDAO.save(permission);
    }

    @Override
    public void deletePermission(int permissionId) {
        permissionDAO.deleteById(permissionId);
    }

    @Override
    public Permission findByPermissionName(String permissionName) {
        return permissionDAO.findByPermissionName(permissionName);
    }

    @Override
    public List<String> getRolesByPermission(int permissionId) {
        return null;
    }
}