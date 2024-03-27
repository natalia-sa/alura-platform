package com.alura.platform.business.course.dto;

import com.alura.platform.business.basic.dto.PaginationDto;
import com.alura.platform.business.course.enums.CourseStatusEnum;

public record CourseFilterDto(CourseStatusEnum status, PaginationDto pagination) {
}
