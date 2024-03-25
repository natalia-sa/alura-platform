package com.alura.platform.business.course.controller;

import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseFilterResponseDto;
import com.alura.platform.business.course.dto.CourseNpsReportDto;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.exception.ActionDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "")
    @Operation(summary = "Save new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating course") })
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
    @Operation(summary = "Inactivate course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course inactivated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while inactivating course") })
    public ResponseEntity<Void> inactivate(
            @RequestParam
            @NotBlank
            @Schema(example = "code")
            String code) {
        try {
            courseService.inactivate(code);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/by/filters")
    @Operation(summary = "List courses by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses list returned successfully",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseFilterResponseDto.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while listing courses") })
    public ResponseEntity<Collection<CourseFilterResponseDto>> findByFilters(
            @RequestParam(required = false)
            @Schema(example = "ACTIVE")
            CourseStatusEnum status,

            @RequestParam
            @Schema(example = "1")
            @NotNull
            Integer page,

            @RequestParam
            @Schema(example = "10")
            @NotNull
            Integer size) {
        try {
            PaginationDto paginationDto = new PaginationDto(page, size);
            CourseFilterDto filtersDto = new CourseFilterDto(status, paginationDto);
            List<CourseFilterResponseDto> courses = courseService.findByFilters(filtersDto);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/nps")
    @Operation(summary = "List courses nps information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses nps information returned successfully",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseNpsReportDto.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while listing courses nps information") })
    public ResponseEntity<Collection<CourseNpsReportDto>> findNpsReport() {
        try {
            List<CourseNpsReportDto> courseNpsReports = courseService.findNpsReport();
            return new ResponseEntity<>(courseNpsReports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
