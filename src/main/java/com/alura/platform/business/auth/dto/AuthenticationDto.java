package com.alura.platform.business.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDto(

        @Schema(example = "joao")
        @NotBlank
        String username,

        @Schema(example = "123")
        @NotBlank
        String password) {
}
