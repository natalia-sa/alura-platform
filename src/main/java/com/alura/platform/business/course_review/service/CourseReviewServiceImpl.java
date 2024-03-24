package com.alura.platform.business.course_review.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.entity.CourseReview;
import com.alura.platform.business.course_review.repository.CourseReviewRepository;
import com.alura.platform.business.email.EmailSender;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.registration.service.RegistrationService;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.service.UserService;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class CourseReviewServiceImpl implements CourseReviewService {

    @Autowired
    private CourseReviewRepository courseReviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RegistrationService registrationService;

    @Override
    public JpaRepository<CourseReview, Long> getRepository() {
        return courseReviewRepository;
    }

    @Override
    @Transactional
    public CourseReview save(CourseReviewDto courseReviewDto) {
        User user = userService.findById(courseReviewDto.userId()).orElseThrow();
        Course course = courseService.findById(courseReviewDto.courseId()).orElseThrow();

        boolean isCourseActive = checkIfCourseIsActive(course);

        if(!isCourseActive) {
            throw new ActionDeniedException("Course is inactive");
        }

        boolean isUserRegisteredInCourse = checkIfUserIsRegisteredInCourse(courseReviewDto.userId(), courseReviewDto.courseId());

        if (!isUserRegisteredInCourse) {
            throw new ActionDeniedException("user is not registered in course");
        }

        CourseReview courseReview = new CourseReview(user, course, courseReviewDto.rating(), courseReviewDto.comment());

        if (courseReview.getRating() < 6) {
            User instructor = userService.findById(course.getInstructor().getId()).orElseThrow();
            sendEmailNotificationToInstructor(courseReview, instructor, course);
        }

        return courseReviewRepository.save(courseReview);
    }

    private boolean checkIfCourseIsActive(Course course) {
        return course.getStatus().equals(CourseStatusEnum.ACTIVE);
    }

    private boolean checkIfUserIsRegisteredInCourse(Long userId, Long courseId) {
        Optional<Registration> registrationOptional = registrationService.findByUserIdCourseId(userId, courseId);
        return registrationOptional.isPresent();
    }

    private void sendEmailNotificationToInstructor(CourseReview courseReview, User instructor, Course course) {
        String recipientEmail = instructor.getEmail();
        String courseName = course.getName();
        String subject = "New review - course %s".formatted(courseName);
        String body = "Course %s received new review. %nRating: %d".formatted(courseName, courseReview.getRating());

        if(courseReview.getComment() != null) {
            body += "%nComment: %s".formatted(courseReview.getComment());
        }

        EmailSender.send(recipientEmail, subject, body);
    }

}
