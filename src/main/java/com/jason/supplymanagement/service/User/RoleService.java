package com.jason.supplymanagement.service.User;

import com.jason.supplymanagement.entity.Users.Permission;
import com.jason.supplymanagement.entity.Users.Role;

import java.util.List;
import java.util.Set;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-22 13:46:27
 * Github: https://github.com/JasonStephen
 */

public interface RoleService {
    // 创建角色
    Role createRole(Role role);

    // 更新角色信息
    Role updateRole(int roleId, Role role);

    // 删除角色
    void deleteRole(int roleId);

    // 为角色绑定权限
    Role bindPermissionToRole(int roleId, int permissionId);

    // 解绑角色与权限的关联
    void unbindPermissionFromRole(int roleId, int permissionId);

    // 查询角色拥有的权限列表
    Set<Permission> getPermissionsByRole(int roleId);

    // 查询拥有该权限的所有角色列表
    List<Role> getRolesByPermission(int permissionId);





    // 分页查询角色列表
    List<Role> listRoles(int page, int size);

    // 根据角色名查询角色
    Role findByRoleName(String roleName);

    // 验证角色是否拥有指定权限
    boolean checkPermissionByRole(int roleId, String code);
}
