package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Permission;
import com.jason.supplymanagement.entity.Role;
import com.jason.supplymanagement.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-22
 */

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // 创建角色
    @PostMapping("/createRole")
    public Role createRole(@RequestParam String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleService.createRole(role);
    }

    // 更新角色信息
    @PutMapping("/updateRole")
    public Role updateRole(int roleId, Role role) {
        return roleService.updateRole(roleId, role);
    }

    // 删除角色
    @DeleteMapping("/deleteRole")
    public void deleteRole(int roleId) {
        roleService.deleteRole(roleId);
    }

    // 绑定权限到角色
    @PostMapping("/bindPermissionToRole")
    public Role bindPermissionToRole(int roleId, int permissionId) {
        return roleService.bindPermissionToRole(roleId, permissionId);
    }

    // 解绑权限从角色
    @DeleteMapping("/unbindPermissionFromRole")
    public void unbindPermissionFromRole(int roleId, int permissionId) {
        roleService.unbindPermissionFromRole(roleId, permissionId);
    }

    // 查询角色拥有的权限列表
    @GetMapping("/getPermissionsByRole")
    public List<Permission> getPermissionsByRole(int roleId) {
        return roleService.getPermissionsByRole(roleId);
    }

    // 分页查询角色列表
    @GetMapping("/listRoles")
    public List<Role> listRoles(int page, int size) {
        return roleService.listRoles(page, size);
    }

    // 验证角色是否拥有指定权限
    @GetMapping("/checkPermissionByRole")
    public boolean checkPermissionByRole(int roleId, String code) {
        return roleService.checkPermissionByRole(roleId, code);
    }

    // 查找角色
    @GetMapping("/findRole")
    public Role findRole(@RequestParam String roleName) {
        return roleService.findByRoleName(roleName);
    }



}
