package com.alura.platform.business.course.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.course.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InactivateTest extends BasicControllerTest {

    private static final String PATH = "/course/inactivate/by/code";

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 200 when course was inactivated with success")
    void shouldReturnSuccessWhenCourseWasInactivatedTest() throws Exception {
        String code = "code";
        Mockito.doNothing().when(courseService).inactivate(code);

        callEndpoint(code).andExpect(status().isOk());

        Mockito.verify(this.courseService, Mockito.times(1)).inactivate(code);
    }

    @Test
    @DisplayName("Should return 400 when required param was not passed, or is blank")
    void shouldReturnBadRequestWhenCodeIsNullOrBlankTest() throws Exception {
        callEndpoint("").andExpect(status().isBadRequest());
        callEndpoint(null).andExpect(status().isBadRequest());
    }

    private ResultActions callEndpoint(String code) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .patch(PATH)
                .queryParam("code", code)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
