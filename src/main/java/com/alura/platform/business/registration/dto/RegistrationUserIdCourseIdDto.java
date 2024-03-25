package com.alura.platform.business.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RegistrationUserIdCourseIdDto(
        @NotNull
        @Schema(example = "1")
        Long userId,

        @NotNull
        @Schema(example = "2")
        Long courseId) {
}
