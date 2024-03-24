package com.alura.platform.business.course.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.basic.PaginationDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseFilterResponseDto;
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

class FindByFilters extends BasicControllerTest {

    private static final String PATH = "/course/by/filters";

    private final Gson gson = new Gson();

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 200 when listing courses")
    void shouldReturnSuccessWhenListingCoursesTest() throws Exception {
        List<CourseFilterResponseDto> response = List.of(
                new CourseFilterResponseDto(
                        1L,
                        "name",
                        "code",
                        1L,
                        "name",
                        CourseStatusEnum.INACTIVE,
                        "description")
        );

        String expectedResponse = gson.toJson(response);

        PaginationDto paginationDto = new PaginationDto(1, 2);
        CourseFilterDto filter = new CourseFilterDto(CourseStatusEnum.ACTIVE, paginationDto);
        Mockito.when(this.courseService.findByFilters(filter)).thenReturn(response);

        MvcResult result = callEndpoint(CourseStatusEnum.ACTIVE, 1, 2).andExpect(status().isOk()).andReturn();
        String responseString = result.getResponse().getContentAsString();
        Mockito.verify(this.courseService, Mockito.times(1)).findByFilters(filter);
        Assertions.assertEquals(expectedResponse, responseString);
    }


    private ResultActions callEndpoint(CourseStatusEnum status, int page, int size) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(PATH)
                .queryParam("status", String.valueOf(status))
                .queryParam("page", String.valueOf(page))
                .queryParam("size", String.valueOf(size))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
