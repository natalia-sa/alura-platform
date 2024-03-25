package com.alura.platform.business.course_review.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.entity.CourseReview;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.registration.service.RegistrationService;
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
    private CourseReviewServiceImpl courseReviewService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        registrationService.deleteAll();
        courseReviewService.deleteAll();
        courseService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("Should save course review successfully")
    void shouldSaveCourseReviewTest() {
        User instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
        User user = userService.save(new User("name", "newuser", "user2@gmail.com", "password", UserRoleEnum.STUDENT));
        Course course = courseService.save(new Course("name", "code", instructor, "description", CourseStatusEnum.ACTIVE));
        Registration registration = registrationService.save(new Registration(user, course));

        CourseReviewDto courseReviewDto = new CourseReviewDto(user.getId(), course.getId(), 8, "great");
        CourseReview courseReview = courseReviewService.save(courseReviewDto);

        Assertions.assertEquals(1, courseReviewService.findAll().size());
        Assertions.assertNotNull(courseReview.getId());
    }
}
