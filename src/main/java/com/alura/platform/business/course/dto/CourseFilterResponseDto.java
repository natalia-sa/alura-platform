package com.alura.platform.business.course.dto;

import java.util.List;

public record CourseFilterResponseDto(List<CourseWithInstructorDataDto> courses, Long totalCount) {
}
