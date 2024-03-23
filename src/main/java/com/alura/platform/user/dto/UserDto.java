package com.alura.platform.user.dto;

import com.alura.platform.user.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserDto(
        @Schema(example = "João")
        String name,

        @Schema(example = "joao")
        String username,

        @Schema(example = "joao@gmail.com")
        String email,

        @Schema(example = "joao123")
        String password,

        @Schema(example = "STUDENT")
        UserRoleEnum role) {
}
