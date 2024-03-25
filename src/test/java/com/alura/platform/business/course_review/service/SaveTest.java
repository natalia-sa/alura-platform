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
    private CourseReviewServiceImpl courseReviewService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    private User user;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private Course course;

    @BeforeEach
    @Transactional
    void setUp() {
        User instructor = makeUser("username", "user@gmail.com", UserRoleEnum.INSTRUCTOR);
        user = makeUser("newuser", "user2@gmail.com", UserRoleEnum.STUDENT);
        user3 = makeUser("joao", "user3@gmail.com", UserRoleEnum.STUDENT);
        user4 = makeUser("ana", "user4@gmail.com", UserRoleEnum.STUDENT);
        user5 = makeUser("maria", "user5@gmail.com", UserRoleEnum.STUDENT);
        user6 = makeUser("clara", "user6@gmail.com", UserRoleEnum.STUDENT);

        course = courseService.save(new Course("name", "code", instructor, "description", CourseStatusEnum.ACTIVE));

        registrationService.save(new Registration(user, course));
        registrationService.save(new Registration(user3, course));
        registrationService.save(new Registration(user4, course));
        registrationService.save(new Registration(user5, course));
        registrationService.save(new Registration(user6, course));
    }

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
        CourseReviewDto courseReviewDto = new CourseReviewDto(user.getId(), course.getId(), 8, "great");
        CourseReview courseReview = courseReviewService.save(courseReviewDto);

        Assertions.assertEquals(1, courseReviewService.findAll().size());
        Assertions.assertNotNull(courseReview.getId());
    }

    @Test
    @DisplayName("Should throw ActionDeniedException when user is not registered in course")
    void shouldThrowActionDeniedExceptionTest() {
        User userNotRegistered = makeUser("new", "new@gmial.com", UserRoleEnum.STUDENT);
        CourseReviewDto courseReviewDto = new CourseReviewDto(userNotRegistered.getId(), course.getId(), 8, "great");
        Assertions.assertThrows(ActionDeniedException.class, () -> courseReviewService.save(courseReviewDto));
    }

    @Test
    @DisplayName("Should update course nps after saving new course review")
    void shouldUpdateCourseNpsTest() {
        CourseReviewDto courseReviewDto1 = new CourseReviewDto(user.getId(), course.getId(), 9, "great");
        CourseReviewDto courseReviewDto2 = new CourseReviewDto(user3.getId(), course.getId(), 10, "great");
        CourseReviewDto courseReviewDto3 = new CourseReviewDto(user4.getId(), course.getId(), 9, "great");
        CourseReviewDto courseReviewDto4 = new CourseReviewDto(user5.getId(), course.getId(), 5, "great");
        CourseReviewDto courseReviewDto5 = new CourseReviewDto(user6.getId(), course.getId(), 8, "great");

        courseReviewService.save(courseReviewDto1);
        courseReviewService.save(courseReviewDto2);
        courseReviewService.save(courseReviewDto3);
        courseReviewService.save(courseReviewDto4);
        courseReviewService.save(courseReviewDto5);

        course = courseService.findById(course.getId()).orElseThrow();

        Assertions.assertEquals(40, course.getNps());
    }

    private User makeUser(String username, String email, UserRoleEnum role) {
        return userService.save(new User("name", username, email, "password", role));
    }
}
