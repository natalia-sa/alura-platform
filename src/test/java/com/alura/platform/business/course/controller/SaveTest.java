package com.alura.platform.business.course.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.course.dto.CourseDto;
import com.alura.platform.business.course.entity.Course;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.exception.ActionDeniedException;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SaveTest extends BasicControllerTest {

    private static final String PATH = "/course";

    private final Gson gson = new Gson();

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 201 when course was created with success")
    void shouldReturnSuccessWhenCourseWasCreatedSuccessfullyTest() throws Exception {
        CourseDto courseDto = makeCourseDto("course", "co-course", 1L, "the new course");

        Course course = new Course(courseDto);

        Mockito.when(this.courseService.save(courseDto)).thenReturn(course);

        callEndpoint(courseDto).andExpect(status().isCreated());

        Mockito.verify(this.courseService, Mockito.times(1)).save(courseDto);
    }

    @Test
    @DisplayName("Should return 403 when ActionDeniedException is thrown")
    void shouldReturnActionDeniedWhenActionDeniedExceptionIsThrownTest() throws Exception {
        CourseDto courseDto = makeCourseDto("course", "course", 1L, "the new course");

        Mockito.when(courseService.save(courseDto))
                .thenThrow(new ActionDeniedException("user is not instructor"));

        callEndpoint(courseDto).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource(value = "returnInvalidCode")
    @DisplayName("Should return 400 when invalid code is received")
    void shouldReturnBadRequestWhenCodeIsInvalid(String code) throws Exception {
        CourseDto courseDto = makeCourseDto("course", code, 1L, "the new course");
        callEndpoint(courseDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnInvalidCode() {
        return Stream.of(
                Arguments.of("courseverynice"),
                Arguments.of("course c"),
                Arguments.of("course1"),
                Arguments.of("course&")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "returnBlankParam")
    @DisplayName("Should return 400 when blank param is received")
    void shouldReturnBadRequestWhenParamIsBlankTest(
            String name, String code, Long instructorId, String description) throws Exception {

        CourseDto courseDto = makeCourseDto(name, code, instructorId, description);
        callEndpoint(courseDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnBlankParam() {
        return Stream.of(
                Arguments.of("", "code", 1L, "good course"),
                Arguments.of("course", "", 1L, "good course"),
                Arguments.of("course", "code", 1L, "")
        );
    }

    private ResultActions callEndpoint(CourseDto courseDto) throws Exception {
        String json = gson.toJson(courseDto);

        return mockMvc.perform(MockMvcRequestBuilders
                .post(PATH)
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    private CourseDto makeCourseDto(String name, String code, Long instructorId, String description) {
        return new CourseDto(name, code, instructorId, description);
    }
}
