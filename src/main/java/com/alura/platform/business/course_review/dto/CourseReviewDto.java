package com.alura.platform.business.course_review.dto;

public record CourseReviewDto(Long userId, Long courseId, Integer rating, String comment) {
}
