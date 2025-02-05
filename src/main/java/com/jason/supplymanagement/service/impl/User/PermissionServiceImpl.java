package com.jason.supplymanagement.service.impl.User;

import com.jason.supplymanagement.entity.Users.Permission;
import com.jason.supplymanagement.dao.Users.PermissionDAO;
import com.jason.supplymanagement.service.User.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;

    @Override
    public Permission addPermission(Permission permission) {
        if (permissionDAO.findByPermissionName(permission.getPermissionName()) != null) {
            throw new RuntimeException("Permission name already exists");
        }
        if (permissionDAO.findByPermissionCode(permission.getPermissionCode()) != null) {
            throw new RuntimeException("Permission code already exists");
        }
        return permissionDAO.save(permission);
    }

    @Override
    public Permission getPermission(int permissionId) {
        return permissionDAO.findById(permissionId).orElse(null);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionDAO.findAll();
    }

    @Override
    public void updatePermission(Permission permission) {
        Permission existingPermission = permissionDAO.findById(permission.getPermissionId()).orElse(null);
        if (existingPermission == null) {
            throw new RuntimeException("Permission not found");
        }

        if (!existingPermission.getPermissionName().equals(permission.getPermissionName())) {
            if (permissionDAO.findByPermissionName(permission.getPermissionName()) != null) {
                throw new RuntimeException("Permission name already exists");
            }
            existingPermission.setPermissionName(permission.getPermissionName());
        }

        if (!existingPermission.getPermissionCode().equals(permission.getPermissionCode())) {
            if (permissionDAO.findByPermissionCode(permission.getPermissionCode()) != null) {
                throw new RuntimeException("Permission code already exists");
            }
            existingPermission.setPermissionCode(permission.getPermissionCode());
        }

        permissionDAO.save(existingPermission);
    }

    @Override
    public void deletePermission(int permissionId) {
        permissionDAO.deleteById(permissionId);
    }

    @Override
    public Permission findByPermissionName(String permissionName) {
        return permissionDAO.findByPermissionName(permissionName);
    }
}