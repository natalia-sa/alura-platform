package com.alura.platform.business.course.service;

import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.entity.Course;
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
class SaveTest {

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
    @DisplayName("Should save course successfully")
    void shouldSaveCourseTest() {
        User instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
        CourseDto courseDto = makeCourseDto("course", "co-course", instructor.getId(), "the new course");

        Course course = courseService.save(courseDto);
        CourseDto savedCourseDto = new CourseDto(course);

        Assertions.assertNotNull(course.getId());
        Assertions.assertEquals(1, courseService.findAll().size());
        Assertions.assertEquals(courseDto, savedCourseDto);
    }

    private CourseDto makeCourseDto(String name, String code, Long instructorId, String description) {
        return new CourseDto(name, code, instructorId, description);
    }
}
