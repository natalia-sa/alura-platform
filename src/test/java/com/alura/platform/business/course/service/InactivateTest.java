package com.alura.platform.business.course.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test"})
class InactivateTest {

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private UserService userService;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        courseService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("Should update course status to INACTIVE")
    void shouldInactivateCourseTest() {
        User instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
        Course course = courseService.save(new Course("name", "code", instructor, "description", CourseStatusEnum.ACTIVE));

        courseService.inactivate(course.getCode());
        course = courseService.findById(course.getId()).orElseThrow();

        Assertions.assertEquals(CourseStatusEnum.INACTIVE, course.getStatus());
    }
}
