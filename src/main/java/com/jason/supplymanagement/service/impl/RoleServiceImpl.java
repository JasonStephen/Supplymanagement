package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.PermissionDAO;
import com.jason.supplymanagement.dao.RoleDAO;
import com.jason.supplymanagement.dao.RolePermissionDAO;
import com.jason.supplymanagement.entity.Permission;
import com.jason.supplymanagement.entity.Role;
import com.jason.supplymanagement.entity.RolePermission;
import com.jason.supplymanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-22
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PermissionDAO permissionDAO;

    @Autowired
    private RolePermissionDAO rolePermissionDAO;

    @Override
    @Transactional
    public Role createRole(Role role) {
        return roleDAO.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(int roleId, Role role) {
        Role oldRole = roleDAO.findById(roleId).orElse(null);
        if (oldRole == null) {
            throw new RuntimeException("角色不存在");
        }
        role.setRoleId(roleId);
        return roleDAO.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(int roleId) {
        roleDAO.deleteById(roleId);
    }

    @Override
    @Transactional
    public Role bindPermissionToRole(int roleId, int permissionId) {
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        Permission permission = permissionDAO.findById(permissionId).orElse(null);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        role.getPermissions().add(permission);
        return roleDAO.save(role);
    }

    @Override
    @Transactional
    public void unbindPermissionFromRole(int roleId, int permissionId) {
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        Permission permission = permissionDAO.findById(permissionId).orElse(null);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        RolePermission rolePermission = rolePermissionDAO.findByRole_RoleIdAndPermission_PermissionId(roleId, permissionId);
        if (rolePermission == null) {
            throw new RuntimeException("角色未绑定该权限");
        }
        role.getPermissions().remove(permission);
        roleDAO.save(role);
    }

    @Override
    public Set<Permission> getPermissionsByRole(int roleId) {
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role != null) {
            return role.getPermissions();
        }
        return null;
    }

    @Override
    public List<Role> getRolesByPermission(int permissionId) {
        List<RolePermission> rolePermissions = rolePermissionDAO.findByPermission_PermissionId(permissionId);
        return rolePermissions.stream()
                .map(RolePermission::getRole)
                .collect(Collectors.toList());
    }


    @Override
    public List<Role> listRoles(int page, int size) {
        return roleDAO.findAll();
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleDAO.findByRoleName(roleName);
    }

    @Override
    public boolean checkPermissionByRole(int roleId, String code) {
        Role role = roleDAO.findById(roleId).orElse(null);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        for (Permission permission : role.getPermissions()) {
            if (permission.getPermissionCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
