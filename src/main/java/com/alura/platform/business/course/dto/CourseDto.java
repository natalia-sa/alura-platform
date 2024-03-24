package com.alura.platform.business.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseDto(
        @Schema(example = "IA course")
        @NotBlank
        String name,

        @Schema(example = "ia-course")
        @NotBlank
        @Size(max = 10)
        @Pattern(regexp = "^[a-zA-Z]+(?:-[a-zA-Z]+)*$")
        String code,

        @Schema(example = "1")
        @NotNull
        Long instructorId,

        @Schema(example = "IA course to beginners")
        @NotBlank
        String description) {
}
