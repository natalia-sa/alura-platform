package com.alura.platform.business.user.dto;

import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.projections.UserNameEmailRoleProjection;

public record UserNameEmailRoleDto(String name, String email, UserRoleEnum role) {

    public UserNameEmailRoleDto(UserNameEmailRoleProjection userProjection) {
        this(userProjection.getName(), userProjection.getEmail(), userProjection.getRole());
    }

    public UserNameEmailRoleDto(User user) {
        this(user.getName(), user.getEmail(), user.getRole());
    }

}
