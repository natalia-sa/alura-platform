package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseFilterResponseDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
class FindByFilters {


    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private UserService userService;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        courseService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("Should list courses according to filters")
    void shouldListCoursesByFiltersTest() {
        User instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
        Course course1 = courseService.save(new Course("name", "codea", instructor, "description", CourseStatusEnum.ACTIVE));
        Course course2 = courseService.save(new Course("name", "codeb", instructor, "description", CourseStatusEnum.ACTIVE));
        Course course3 = courseService.save(new Course("name", "codec", instructor, "description", CourseStatusEnum.INACTIVE));

        PaginationDto paginationDto = new PaginationDto(1, 2);
        CourseFilterDto filter = new CourseFilterDto(null, paginationDto);

        List<CourseFilterResponseDto> courses = courseService.findByFilters(filter);
        Assertions.assertEquals(2, courses.size());
    }
}
