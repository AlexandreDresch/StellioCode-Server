package com.stelliocode.backend.entity;

public enum UserRole {
    ADMIN("admin"),
    DEVELOPER("developer");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
