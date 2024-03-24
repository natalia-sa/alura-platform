package com.alura.platform.business.course.controller;

import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.exception.ActionDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "")
    @Operation(summary = "Save new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course was created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "500", description = "Something went wrong while creating course",
                    content = @Content) })
    public ResponseEntity save(
            @RequestBody
            @Valid
            CourseDto courseDto) {
        try {
            Course course = courseService.save(courseDto);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/inactivate/by/code")
    @Operation(summary = "Update course status to INACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course was inactivated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "500", description = "Something went wrong while inactivating course",
                    content = @Content) })
    public ResponseEntity inactivate(
            @RequestParam
            @NotBlank
            String code) {
        try {
            courseService.inactivate(code);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
