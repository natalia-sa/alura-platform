package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.dto.CourseNpsDto;
import com.alura.platform.business.course.dto.CourseNpsTotalCountDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
class FindNpsReportTest {

    private User instructor;

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private UserService userService;

    @BeforeEach
    @Transactional
    void setUp() {
        instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
    }

    @Test
    @DisplayName("Should list courses with nps information")
    @Transactional
    void shouldListCoursesNpsInformationTest() {
        PaginationDto paginationDto = new PaginationDto(1, 10);
        Course course1 = makeCourse("abc", null);
        Course course2 = makeCourse("abcd", 20);
        Course course3 = makeCourse("abce", 10);

        List<CourseNpsDto> expected = List.of(
                new CourseNpsDto(course2),
                new CourseNpsDto(course3));

        CourseNpsTotalCountDto courses = courseService.findNpsReport(paginationDto);
        Assertions.assertEquals(2, courses.totalCount());
        Assertions.assertTrue(courses.courses().containsAll(expected));
    }

    private Course makeCourse(String code, Integer nps) {
        return courseService.save(new Course("name", code, instructor, "description", CourseStatusEnum.ACTIVE, nps));
    }
}
