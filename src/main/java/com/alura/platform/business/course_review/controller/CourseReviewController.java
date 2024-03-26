package com.alura.platform.business.course_review.controller;

import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.service.CourseReviewService;
import com.alura.platform.exception.ActionDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course-review")
public class CourseReviewController {

    @Autowired
    private CourseReviewService courseReviewService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "")
    @Operation(summary = "Save new course review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course review created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating course review") })
    public ResponseEntity save(
            @RequestBody
            @Valid
            CourseReviewDto courseReviewDto) {
        try {
            courseReviewService.save(courseReviewDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
