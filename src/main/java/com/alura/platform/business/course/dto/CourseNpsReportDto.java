package com.alura.platform.business.course.dto;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;

public record CourseNpsReportDto(Long id, String name, CourseStatusEnum status, Long instructorId, Integer nps) {

    public CourseNpsReportDto(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getStatus(),
                course.getInstructor().getId(),
                course.getNps());
    }
}
