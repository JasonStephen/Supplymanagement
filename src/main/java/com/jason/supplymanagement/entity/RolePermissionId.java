package com.jason.supplymanagement.entity;

import java.io.Serializable;
import java.util.Objects;

public class RolePermissionId implements Serializable {

    private int role; // 对应 Role 表的 role_id
    private int permission; // 对应 Permission 表的 permission_id

    // 默认构造函数
    public RolePermissionId() {}

    // 带参构造函数
    public RolePermissionId(int role, int permission) {
        this.role = role;
        this.permission = permission;
    }

    // Getters and Setters
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionId that = (RolePermissionId) o;
        return role == that.role && permission == that.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, permission);
    }
}