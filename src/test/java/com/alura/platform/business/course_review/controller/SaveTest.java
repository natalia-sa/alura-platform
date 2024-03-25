package com.alura.platform.business.course_review.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.course_review.dto.CourseReviewDto;
import com.alura.platform.business.course_review.entity.CourseReview;
import com.alura.platform.business.course_review.service.CourseReviewService;
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

    private static final String PATH = "/course-review";

    private final Gson gson = new Gson();

    @MockBean
    private CourseReviewService courseReviewService;

    @Test
    @DisplayName("Should return 201 when course review was created with success")
    void shouldReturnSuccessWhenCourseReviewWasCreatedSuccessfullyTest() throws Exception {
        CourseReviewDto courseReviewDto = makeCourseReviewDto(1L, 1L, 1, "bad course");

        CourseReview courseReview = new CourseReview();

        Mockito.when(this.courseReviewService.save(courseReviewDto)).thenReturn(courseReview);

        callEndpoint(courseReviewDto).andExpect(status().isCreated());

        Mockito.verify(this.courseReviewService, Mockito.times(1)).save(courseReviewDto);
    }

    @ParameterizedTest
    @MethodSource(value = "returnInvalidParams")
    @DisplayName("Should return 400 when invalid rating values are passed as input")
    void shouldReturnBadRequestWhenInvalidValuesAreReceivedTest(Integer rating) throws Exception {
        CourseReviewDto courseReviewDto = makeCourseReviewDto(1L, 1L, rating, "bad course");
        callEndpoint(courseReviewDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnInvalidParams() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(11)
        );
    }

    private ResultActions callEndpoint(CourseReviewDto courseReviewDto) throws Exception {
        String json = gson.toJson(courseReviewDto);

        return mockMvc.perform(MockMvcRequestBuilders
                .post(PATH)
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    private CourseReviewDto makeCourseReviewDto(Long userId, Long courseId, Integer rating, String comment) {
        return new CourseReviewDto(userId, courseId, rating, comment);
    }
}
