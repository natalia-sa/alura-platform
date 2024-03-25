package com.alura.platform.business.course.service;

import com.alura.platform.business.course.dto.CourseNpsReportDto;
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
    @DisplayName("Should list courses nps information")
    @Transactional
    void shouldListCoursesNpsInformationTest() {
        Course course1 = makeCourse("abc", null);
        Course course2 = makeCourse("abcd", 20);
        Course course3 = makeCourse("abce", 10);

        List<CourseNpsReportDto> expected = List.of(
                new CourseNpsReportDto(course1),
                new CourseNpsReportDto(course2),
                new CourseNpsReportDto(course3));

        List<CourseNpsReportDto> courses = courseService.findNpsReport();
        Assertions.assertEquals(3, courses.size());
        Assertions.assertTrue(courses.containsAll(expected));
    }

    private Course makeCourse(String code, Integer nps) {
        return courseService.save(new Course("name", code, instructor, "description", CourseStatusEnum.ACTIVE, nps));
    }
}
