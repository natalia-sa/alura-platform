package com.alura.platform.business.registration.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
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

    private User instructor;

    @BeforeEach
    @Transactional
    void setUp() {
        instructor = makeUser("username", "user@gmail.com", UserRoleEnum.INSTRUCTOR);
    }

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
        User user = makeUser("newuser", "user2@gmail.com", UserRoleEnum.STUDENT);
        Course course = makeCourse(CourseStatusEnum.ACTIVE);

        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(user.getId(), course.getId());
        Registration registration = registrationService.save(registrationDto);

        Assertions.assertEquals(1, registrationService.findAll().size());
        Assertions.assertNotNull(registration.getId());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when course is inactive")
    void shouldThrowActionDeniedExceptionWhenCourseIsInactiveTest() {
        User user = makeUser("newuser", "user2@gmail.com", UserRoleEnum.STUDENT);
        Course course = makeCourse(CourseStatusEnum.INACTIVE);

        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(user.getId(), course.getId());

        Assertions.assertThrows(ActionDeniedException.class, () -> registrationService.save(registrationDto));
    }

    @Test
    @DisplayName("Should throw ActionDeniedException when user is already registered in course")
    void shouldThrowActionDeniedExceptionWhenUserIsAlreadyRegisteredTest() {
        User user = makeUser("newuser", "user2@gmail.com", UserRoleEnum.STUDENT);
        Course course = makeCourse(CourseStatusEnum.ACTIVE);

        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(user.getId(), course.getId());
        registrationService.save(registrationDto);

        RegistrationUserIdCourseIdDto registrationDto2 = new RegistrationUserIdCourseIdDto(user.getId(), course.getId());

        Assertions.assertThrows(ActionDeniedException.class, () -> registrationService.save(registrationDto2));
    }

    private User makeUser(String username, String email, UserRoleEnum role) {
        return userService.save(new User("name", username, email, "password", role));
    }

    private Course makeCourse(CourseStatusEnum status) {
        return courseService.save(new Course("name", "code", instructor, "description", status));
    }
}
