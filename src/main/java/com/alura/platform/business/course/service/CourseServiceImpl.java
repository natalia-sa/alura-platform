package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.dto.PaginationDto;
import com.alura.platform.business.course.dto.*;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.repository.CourseFilterRepository;
import com.alura.platform.business.course.repository.CourseRepository;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseFilterRepository courseFilterRepository;

    @Autowired
    private UserService userService;

    @Override
    public JpaRepository<Course, Long> getRepository() {
        return courseRepository;
    }

    @Transactional
    @Override
    public Course save(CourseDto courseDto) {
        User user = userService.findById(courseDto.instructorId()).orElseThrow();

        validateSaveRequest(user, courseDto);

        Course course = new Course(courseDto.name(), courseDto.code(), user, courseDto.description(), CourseStatusEnum.ACTIVE);
        return courseRepository.save(course);
    }

    private void validateSaveRequest(User user, CourseDto courseDto) {
        validateIfUserIsInstructor(user);
        validateIfCourseCodeAlreadyExists(courseDto);
    }

    private void validateIfCourseCodeAlreadyExists(CourseDto courseDto) {
        Optional<Course> courseOptional = courseRepository.findByCode(courseDto.code());

        if(courseOptional.isPresent()) {
            throw new ActionDeniedException("This course code is already in use");
        }
    }

    private void validateIfUserIsInstructor(User user) {
        boolean isUserInstructor = checkIfUserIsInstructor(user);

        if(!isUserInstructor) {
            throw new ActionDeniedException("User is not instructor");
        }
    }

    private boolean checkIfUserIsInstructor(User user) {
        return user.getRole().equals(UserRoleEnum.INSTRUCTOR);
    }

    @Transactional
    @Override
    public void inactivate(String code) {
        Course course = courseRepository.findByCode(code).orElseThrow();
        course.setStatus(CourseStatusEnum.INACTIVE);
    }

    @Override
    public CourseListTotalCountDto findByFilters(CourseFilterDto filter) {
        List<Long> courseIds = courseFilterRepository.findIdByFilters(filter.status(), null, filter.pagination());
        Long count = courseFilterRepository.countByFilters(filter.status(), null);

        List<CourseWithInstructorDataDto> courses = courseIds
                .stream()
                .map(courseId -> {
                    Course course = courseRepository.findById(courseId).orElseThrow();
                    User instructor = userService.findById(course.getInstructor().getId()).orElseThrow();
                    return new CourseWithInstructorDataDto(course, instructor);})
                .toList();

        return new CourseListTotalCountDto(courses, count);
    }

    @Override
    public CourseNpsTotalCountDto findNpsReport(PaginationDto pagination) {
        List<Long> courseIds = courseFilterRepository.findIdByFilters(null, true, pagination);
        Long count = courseFilterRepository.countByFilters(null, true);

        List<CourseNpsDto> courses = courseIds
                .stream()
                .map(courseId -> {
                    Course course = courseRepository.findById(courseId).orElseThrow();
                    return new CourseNpsDto(course);})
                .toList();

        return new CourseNpsTotalCountDto(courses, count);
    }

}
