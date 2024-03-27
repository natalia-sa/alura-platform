package com.alura.platform.business.course_review.service;

import com.alura.platform.business.basic.service.BasicService;
import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.entity.CourseReview;

public interface CourseReviewService  extends BasicService<CourseReview, Long> {

    CourseReview save(CourseReviewDto courseReviewDto);
}
