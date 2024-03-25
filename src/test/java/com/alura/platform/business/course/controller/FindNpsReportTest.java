package com.alura.platform.business.course.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.course.dto.CourseNpsReportDto;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FindNpsReportTest extends BasicControllerTest {

    private static final String PATH = "/course/nps";

    private final Gson gson = new Gson();

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 200 when listing courses nps information")
    void shouldReturnSuccessWhenListingCourseNpsInformationTest() throws Exception {
        List<CourseNpsReportDto> response = List.of(
                new CourseNpsReportDto(
                        1L, "name", CourseStatusEnum.INACTIVE, 1L, 10)
        );

        String expectedResponse = gson.toJson(response);

        Mockito.when(this.courseService.findNpsReport()).thenReturn(response);

        MvcResult result = callEndpoint().andExpect(status().isOk()).andReturn();
        String responseString = result.getResponse().getContentAsString();

        Mockito.verify(this.courseService, Mockito.times(1)).findNpsReport();
        Assertions.assertEquals(expectedResponse, responseString);
    }

    private ResultActions callEndpoint() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
