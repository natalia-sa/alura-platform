package com.alura.platform.business.course.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.basic.SecurityContextTestUtils;
import com.alura.platform.business.basic.dto.PaginationDto;
import com.alura.platform.business.course.dto.CourseFilterDto;
import com.alura.platform.business.course.dto.CourseListTotalCountDto;
import com.alura.platform.business.course.enums.CourseStatusEnum;
import com.alura.platform.business.course.service.CourseService;
import com.alura.platform.business.user.enums.UserRoleEnum;
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

class FindByFiltersTest extends BasicControllerTest {

    private static final String PATH = "/course/by/filters";

    private final Gson gson = new Gson();

    @MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 200 when listing courses")
    void shouldReturnSuccessWhenListingCoursesTest() throws Exception {
        SecurityContextTestUtils.fakeAuthentication(UserRoleEnum.ADMIN);
        CourseListTotalCountDto response = new CourseListTotalCountDto(List.of(), 0L);

        String expectedResponse = gson.toJson(response);

        PaginationDto paginationDto = new PaginationDto(1, 2);
        CourseFilterDto filter = new CourseFilterDto(CourseStatusEnum.ACTIVE, paginationDto);
        Mockito.when(this.courseService.findByFilters(filter)).thenReturn(response);

        MvcResult result = callEndpoint(filter).andExpect(status().isOk()).andReturn();
        String responseString = result.getResponse().getContentAsString();
        Mockito.verify(this.courseService, Mockito.times(1)).findByFilters(filter);
        Assertions.assertEquals(expectedResponse, responseString);
    }

    @Test
    @DisplayName("Should return 400 when required information is not passed as parameter")
    void shouldReturnBadRequestWhenListingCoursesTest() throws Exception {
        PaginationDto paginationDto = new PaginationDto(null, 2);
        CourseFilterDto filter = new CourseFilterDto(CourseStatusEnum.ACTIVE, paginationDto);

        callEndpoint(filter).andExpect(status().isBadRequest());

        PaginationDto paginationDto2 = new PaginationDto(1, null);
        CourseFilterDto filter2 = new CourseFilterDto(CourseStatusEnum.ACTIVE, paginationDto2);

        callEndpoint(filter2).andExpect(status().isBadRequest());
    }

    private ResultActions callEndpoint(CourseFilterDto filter) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(PATH)
                .queryParam("status", String.valueOf(filter.status()))
                .queryParam("page", String.valueOf(filter.pagination().page()))
                .queryParam("size", String.valueOf(filter.pagination().size()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
