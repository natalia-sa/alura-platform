package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.service.BasicService;
import com.alura.platform.business.basic.dto.PaginationDto;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseListTotalCountDto;
import com.alura.platform.business.course.dto.CourseNpsTotalCountDto;
import com.alura.platform.business.course.entity.Course;

public interface CourseService extends BasicService<Course, Long> {
    Course save(CourseDto courseDto);

    void inactivate(String code);

    CourseListTotalCountDto findByFilters(CourseFilterDto filter);

    CourseNpsTotalCountDto findNpsReport(PaginationDto pagination);
}
