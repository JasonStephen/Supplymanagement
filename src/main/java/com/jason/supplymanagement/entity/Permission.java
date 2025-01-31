package com.jason.supplymanagement.entity;

import jakarta.persistence.*;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_name"),
        @UniqueConstraint(columnNames = "permission_code")
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;

    @Column(name = "permission_name", nullable = false, length = 50, unique = true)
    private String permissionName;

    @Column(name = "permission_code", nullable = false, length = 50, unique = true)
    private String permissionCode;

    // Getters and Setters
    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}