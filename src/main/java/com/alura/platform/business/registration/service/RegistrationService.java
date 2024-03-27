package com.alura.platform.business.registration.service;

import com.alura.platform.business.basic.service.BasicService;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;

import java.util.Optional;

public interface RegistrationService extends BasicService<Registration, Long> {

    Registration save(RegistrationUserIdCourseIdDto dto);

    Optional<Registration> findByUserIdCourseId(Long userId, Long courseId);
}
