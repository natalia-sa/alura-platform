package com.alura.platform.business.course.dto;

import java.util.List;

public record CourseNpsReportDto(List<CourseNpsDto> courses, Long totalCount) {
}
