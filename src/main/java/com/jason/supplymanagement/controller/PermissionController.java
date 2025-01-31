package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Permission;
import com.jason.supplymanagement.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-22
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // 创建权限
    @PostMapping("/createPermission")
    public Permission createPermission(@RequestParam String permissionName) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
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




    // 查询权限项所绑定的角色列表
    @GetMapping("/getRolesByPermission")
    public String getRolesByPermission(int permissionId) {
        return permissionService.getRolesByPermission(permissionId).toString();
    }

}
