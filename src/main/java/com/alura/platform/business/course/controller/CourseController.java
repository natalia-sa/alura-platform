package com.alura.platform.business.course.controller;

import com.alura.platform.business.basic.IdDto;
import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseListTotalCountDto;
import com.alura.platform.business.course.dto.CourseNpsTotalCountDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "")
    @Operation(summary = "Save new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of the saved entity",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while creating course") })
    public ResponseEntity save(
            @RequestBody
            @Valid
            CourseDto courseDto) {
        try {
            Course course = courseService.save(courseDto);
            IdDto id = new IdDto(course.getId());
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ActionDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/by/filters")
    @Operation(summary = "List courses by filters with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses list returned successfully",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseListTotalCountDto.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while listing courses") })
    public ResponseEntity<CourseListTotalCountDto> findByFilters(
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
            CourseListTotalCountDto courses = courseService.findByFilters(filtersDto);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/nps")
    @Operation(summary = "List courses nps information with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses nps information returned successfully",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseNpsTotalCountDto.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while listing courses nps information") })
    public ResponseEntity<CourseNpsTotalCountDto> findNpsReport(
            @RequestParam
            @Schema(example = "1")
            @NotNull
            Integer page,

            @RequestParam
            @Schema(example = "10")
            @NotNull
            Integer size
    ) {
        try {
            PaginationDto paginationDto = new PaginationDto(page, size);
            CourseNpsTotalCountDto courseNpsReports = courseService.findNpsReport(paginationDto);
            return new ResponseEntity<>(courseNpsReports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
