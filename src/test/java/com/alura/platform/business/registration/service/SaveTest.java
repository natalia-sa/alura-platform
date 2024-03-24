package com.alura.platform.business.registration.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;
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
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationServiceImpl registrationService;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        registrationService.deleteAll();
        courseService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("Should save registration successfully")
    void shouldSaveRegistrationTest() {
        User instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
        User user = userService.save(new User("name", "newuser", "user2@gmail.com", "password", UserRoleEnum.STUDENT));

        Course course = courseService.save(new Course("name", "code", instructor, "description", CourseStatusEnum.ACTIVE));

        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(user.getId(), course.getId());
        Registration registration = registrationService.save(registrationDto);

        Assertions.assertEquals(1, registrationService.findAll().size());
        Assertions.assertNotNull(registration.getId());
    }
}
