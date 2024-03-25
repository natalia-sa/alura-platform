package com.alura.platform.business.course.dto;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;

public record CourseNpsDto(Long id, String name, CourseStatusEnum status, Long instructorId, Integer nps) {

    public CourseNpsDto(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getStatus(),
                course.getInstructor().getId(),
                course.getNps());
    }
}
