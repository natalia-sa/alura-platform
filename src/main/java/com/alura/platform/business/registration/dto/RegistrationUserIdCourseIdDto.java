package com.alura.platform.business.registration.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrationUserIdCourseIdDto(
        @NotNull
        Long userId,

        @NotNull
        Long courseId) {
}
