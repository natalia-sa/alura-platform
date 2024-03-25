package com.alura.platform.business.course_review.controller;

import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.entity.CourseReview;
import com.alura.platform.business.course_review.service.CourseReviewService;
import com.alura.platform.exception.ActionDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course-review")
public class CourseReviewController {

    @Autowired
    private CourseReviewService courseReviewService;

    @PostMapping(value = "")
    @Operation(summary = "Save new course review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course review created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReview.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating course review") })
    public ResponseEntity save(
            @RequestBody
            @Valid
            CourseReviewDto courseReviewDto) {
        try {
            CourseReview courseReview = courseReviewService.save(courseReviewDto);
            return new ResponseEntity<>(courseReview, HttpStatus.CREATED);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
