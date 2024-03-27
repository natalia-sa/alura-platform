package com.alura.platform.business.course.dto;

import java.util.List;

public record CourseNpsTotalCountDto(List<CourseNpsDto> courses, Long totalCount) {
}
