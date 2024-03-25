package com.alura.platform.business.course_review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CourseReviewDto(
        @Schema(example = "1")
        @NotNull
        Long userId,

        @Schema(example = "1")
        @NotNull
        Long courseId,

        @Schema(example = "10")
        @NotNull
        @Max(value = 10)
        @Min(value = 0)
        Integer rating,

        @Schema(example = "Course was really good")
        String comment) {
}
