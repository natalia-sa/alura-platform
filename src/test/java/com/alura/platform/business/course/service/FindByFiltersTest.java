package com.alura.platform.business.course.service;

import com.alura.platform.business.basic.dto.PaginationDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseListTotalCountDto;
import com.alura.platform.business.course.dto.CourseWithInstructorDataDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"test"})
class FindByFiltersTest {

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private UserService userService;

    private User instructor;

    @BeforeEach
    @Transactional
    void setUp() {
        instructor = userService.save(new User("name", "username", "user@gmail.com", "password", UserRoleEnum.INSTRUCTOR));
    }

    @AfterEach
    @Transactional
    void cleanDatabase() {
        courseService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("Should list courses all courses when no filter in passed")
    void shouldListAllCoursesTest() {
        Course course1 = makeCourse("abc", CourseStatusEnum.ACTIVE);
        Course course2 = makeCourse("abcd", CourseStatusEnum.ACTIVE);
        Course course3 = makeCourse("abce", CourseStatusEnum.INACTIVE);

        List<CourseWithInstructorDataDto> expectedCourses = List.of(
                new CourseWithInstructorDataDto(course1, instructor),
                new CourseWithInstructorDataDto(course2, instructor),
                new CourseWithInstructorDataDto(course3, instructor));

        PaginationDto paginationDto = new PaginationDto(1, 5);
        CourseFilterDto filter = new CourseFilterDto(null, paginationDto);

        CourseListTotalCountDto coursesFound = courseService.findByFilters(filter);
        Assertions.assertEquals(3, coursesFound.totalCount());
        Assertions.assertTrue(coursesFound.courses().containsAll(expectedCourses));
    }

    @Test
    @DisplayName("Should list only active courses")
    void shouldListOnlyActiveCoursesTest() {
        Course course1 = makeCourse("abc", CourseStatusEnum.ACTIVE);
        Course course2 = makeCourse("abcd", CourseStatusEnum.ACTIVE);
        makeCourse("abce", CourseStatusEnum.INACTIVE);

        List<CourseWithInstructorDataDto> expectedCourses = List.of(
                new CourseWithInstructorDataDto(course1, instructor),
                new CourseWithInstructorDataDto(course2, instructor));

        PaginationDto paginationDto = new PaginationDto(1, 5);
        CourseFilterDto filter = new CourseFilterDto(CourseStatusEnum.ACTIVE, paginationDto);

        CourseListTotalCountDto coursesFound = courseService.findByFilters(filter);
        Assertions.assertEquals(2, coursesFound.totalCount());
        Assertions.assertTrue(coursesFound.courses().containsAll(expectedCourses));
    }

    @Test
    @DisplayName("Should paginate results correctly")
    void shouldPaginateResultsTest() {
        Course course1 = makeCourse("abc", CourseStatusEnum.ACTIVE);
        Course course2 = makeCourse("abcd", CourseStatusEnum.ACTIVE);
        makeCourse("abce", CourseStatusEnum.INACTIVE);

        List<CourseWithInstructorDataDto> expectedCoursesInPage1 = List.of(
                new CourseWithInstructorDataDto(course1, instructor),
                new CourseWithInstructorDataDto(course2, instructor));

        PaginationDto paginationDto = new PaginationDto(1, 2);
        CourseFilterDto filter = new CourseFilterDto(null, paginationDto);

        CourseListTotalCountDto coursesFound = courseService.findByFilters(filter);
        Assertions.assertEquals(3, coursesFound.totalCount());
        Assertions.assertTrue(coursesFound.courses().containsAll(expectedCoursesInPage1));
    }

    private Course makeCourse(String code, CourseStatusEnum status) {
        return courseService.save(new Course("name", code, instructor, "description", status));
    }
}
