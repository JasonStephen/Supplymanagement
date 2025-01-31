package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Permission;
import com.jason.supplymanagement.entity.Role;
import com.jason.supplymanagement.service.PermissionService;
import com.jason.supplymanagement.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-22
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {
    private final PermissionService permissionService;
    private final RoleService roleService;

    public PermissionController(PermissionService permissionService, RoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    // 创建权限
    @PostMapping("/createPermission")
    public Permission createPermission(@RequestParam String permissionName, @RequestParam String permissionCode) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
        permission.setPermissionCode(permissionCode);
        return permissionService.addPermission(permission);
    }

    // 查询权限详情
    @GetMapping("/getPermission")
    public Permission getPermission(int permissionId) {
        return permissionService.getPermission(permissionId);
    }

    // 查询所有权限
    @GetMapping("/getAllPermissions")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    // 更新权限信息
    @PutMapping("/updatePermission")
    public Permission updatePermission(int permissionId, Permission permission) {
        permissionService.updatePermission(permission);
        return permissionService.getPermission(permissionId);
    }

    // 删除权限项
    @DeleteMapping("/deletePermission")
    public void deletePermission(int permissionId) {
        permissionService.deletePermission(permissionId);
    }




    // 获取权限项所绑定的角色列表
    @GetMapping("/getPermissionsByRole")
    public Set<Permission> getPermissionsByRole(@RequestParam int roleId) {
        return roleService.getPermissionsByRole(roleId);
    }

    //绑定权限到角色
    @PostMapping("/bindPermissionToRole")
    public void bindPermissionToRole(@RequestParam int permissionId, @RequestParam int roleId) {
        roleService.bindPermissionToRole(permissionId, roleId);
    }

    // 获取权限项所绑定的角色列表
    @GetMapping("/getRolesByPermission")
    public List<Role> getRolesByPermission(@RequestParam int permissionId) {
        return roleService.getRolesByPermission(permissionId);
    }



}
