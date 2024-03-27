package com.alura.platform.business.registration.service;

import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.registration.repository.RegistrationRepository;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.service.UserService;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Override
    public JpaRepository<Registration, Long> getRepository() {
        return registrationRepository;
    }

    @Transactional
    @Override
    public Registration save(RegistrationUserIdCourseIdDto dto) {
        User user = userService.findById(dto.userId()).orElseThrow();
        Course course = courseService.findById(dto.courseId()).orElseThrow();

        validateSaveRequest(course, dto);

        Registration registration = new Registration(user, course);
        return registrationRepository.save(registration);
    }

    private void validateSaveRequest(Course course, RegistrationUserIdCourseIdDto registrationDto)  {
        validateIfCourseIsActive(course);
        validateIfUserIsAlreadyRegisteredInCourse(registrationDto);
    }

    private void validateIfUserIsAlreadyRegisteredInCourse(RegistrationUserIdCourseIdDto dto) {
        Optional<Registration> registrationOptional = registrationRepository.findByUserIdAndCourseId(dto.userId(), dto.courseId());

        if(registrationOptional.isPresent()) {
            throw new ActionDeniedException("User is already registered in this course");
        }
    }

    private void validateIfCourseIsActive(Course course) {
        boolean isCourseActive = checkIfCourseIsActive(course);

        if(!isCourseActive) {
            throw new ActionDeniedException("Course is inactive");
        }
    }

    @Override
    public Optional<Registration> findByUserIdCourseId(Long userId, Long courseId) {
        return registrationRepository.findByUserIdAndCourseId(userId, courseId);
    }

    private boolean checkIfCourseIsActive(Course course) {
        return course.getStatus().equals(CourseStatusEnum.ACTIVE);
    }
}
