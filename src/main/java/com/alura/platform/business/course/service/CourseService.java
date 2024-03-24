package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.BasicService;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.repository.CourseRepository;
import com.alura.platform.exception.ActionDeniedException;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService implements BasicService<Course, Long> {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Override
    public JpaRepository<Course, Long> getRepository() {
        return courseRepository;
    }

    @Transactional
    public Course save(CourseDto courseDto) {
        User user = userService.findById(courseDto.instructorId()).orElseThrow();
        boolean isUserInstructor = checkIfUserIsInstructor(user);

        if(!isUserInstructor) {
            throw new ActionDeniedException("User is not instructor");
        }

        Course course = new Course(courseDto.name(), courseDto.code(), user, courseDto.description(), CourseStatusEnum.ACTIVE);
        return courseRepository.save(course);
    }

    private boolean checkIfUserIsInstructor(User user) {
        return user.getRole().equals(UserRoleEnum.INSTRUCTOR);
    }

    @Transactional
    public void inactivate(String code) {
        Course course = courseRepository.findByCode(code).orElseThrow();
        course.setStatus(CourseStatusEnum.INACTIVE);
    }
}
