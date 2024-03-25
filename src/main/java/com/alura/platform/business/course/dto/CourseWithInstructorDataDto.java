package com.alura.platform.business.course.dto;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;

public record CourseWithInstructorDataDto(
        Long id,
        String name,
        String code,
        Long instructorId,
        String instructorName,
        CourseStatusEnum status,
        String description) {
    public CourseWithInstructorDataDto(Course course, User instructor) {
        this(
                course.getId(),
                course.getName(),
                course.getCode(),
                instructor.getId(),
                instructor.getName(),
                course.getStatus(),
                course.getDescription());
    }

}
