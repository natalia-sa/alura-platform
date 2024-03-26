package com.alura.platform.business.user.enums;

public enum UserRoleEnum {
    STUDENT("student"),
    INSTRUCTOR("instructor"),
    ADMIN("admin");

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    private String getRole() {
        return role;
    }
}
