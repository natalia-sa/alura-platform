package com.alura.platform.business.course.repository;

import com.alura.platform.business.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            SELECT course
            FROM Course course
            WHERE course.code = :code""" )
    Optional<Course> findByCode(@Param("code") String code);
}
