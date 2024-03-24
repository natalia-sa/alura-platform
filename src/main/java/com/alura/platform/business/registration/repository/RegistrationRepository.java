package com.alura.platform.business.registration.repository;

import com.alura.platform.business.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("""
            SELECT registration
            FROM Registration registration
            WHERE registration.user.id = :userId
            AND registration.course.id = :courseId""" )
    Optional<Registration> findByUserIdCourseId(@Param("userId") Long userId, @Param("courseId")  Long courseId);
}
