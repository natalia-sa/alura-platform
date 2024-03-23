package com.alura.platform.user.dto;

import com.alura.platform.user.enums.UserRoleEnum;

public record UserDto(String name, String username, String email, String password, UserRoleEnum role) {
}
