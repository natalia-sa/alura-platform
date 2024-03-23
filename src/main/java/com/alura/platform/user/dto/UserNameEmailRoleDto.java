package com.alura.platform.user.dto;

import com.alura.platform.user.enums.UserRoleEnum;
import com.alura.platform.user.projections.UserNameEmailRoleProjection;

public record UserNameEmailRoleDto(String name, String email, UserRoleEnum role) {

    public UserNameEmailRoleDto(UserNameEmailRoleProjection userProjection) {
        this(userProjection.getName(), userProjection.getEmail(), userProjection.getRole());
    }

}
