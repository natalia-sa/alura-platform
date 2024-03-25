package com.alura.platform.business.course_review.repository;

import com.alura.platform.business.course_review.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    @Query("""
            SELECT courseReview
            FROM CourseReview courseReview
            WHERE courseReview.course.id = :courseId""" )
    Set<CourseReview> findByCourseId(@Param("courseId") Long courseId);
}
