package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.BasicService;
import com.alura.platform.business.course.dto.*;
import com.alura.platform.business.course.entity.Course;

import java.util.List;

public interface CourseService extends BasicService<Course, Long> {
    Course save(CourseDto courseDto);

    void inactivate(String code);

    CourseFilterResponseDto findByFilters(CourseFilterDto filter);

    List<CourseNpsReportDto> findNpsReport();
}
