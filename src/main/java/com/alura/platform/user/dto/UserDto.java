package com.alura.platform.user.dto;

import com.alura.platform.user.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDto(
        @Schema(example = "João")
        @NotBlank
        String name,

        @Schema(example = "joao")
        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^[a-z]+$")
        String username,

        @Schema(example = "joao@gmail.com")
        @Email(regexp = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        @NotBlank
        String email,

        @Schema(example = "joao123")
        @NotBlank
        String password,

        @Schema(example = "STUDENT")
        UserRoleEnum role) {
}
