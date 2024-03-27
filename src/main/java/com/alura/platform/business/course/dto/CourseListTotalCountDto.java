package com.alura.platform.business.course.dto;

import java.util.List;

public record CourseListTotalCountDto(List<CourseWithInstructorDataDto> courses, Long totalCount) {
}
