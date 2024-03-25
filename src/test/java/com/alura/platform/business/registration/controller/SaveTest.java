package com.alura.platform.business.registration.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.registration.dto.RegistrationUserIdCourseIdDto;
import com.alura.platform.business.registration.entity.Registration;
import com.alura.platform.business.registration.service.RegistrationService;
import com.alura.platform.exception.ActionDeniedException;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SaveTest extends BasicControllerTest {


    private static final String PATH = "/registration";

    private final Gson gson = new Gson();

    @MockBean
    private RegistrationService registrationService;

    @Test
    @DisplayName("Should return 201 when registration was created with success")
    void shouldReturnSuccessWhenRegistrationWasCreatedSuccessfullyTest() throws Exception {
        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(1L, 1L);

        Registration registration = new Registration();

        Mockito.when(this.registrationService.save(registrationDto)).thenReturn(registration);

        callEndpoint(registrationDto).andExpect(status().isCreated());

        Mockito.verify(this.registrationService, Mockito.times(1)).save(registrationDto);
    }

    @Test
    @DisplayName("Should return 400 when invalid data is received")
    void shouldReturnBadRequestWhenInvalidDataIsReceivedTest() throws Exception {
        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(null, 1L);
        callEndpoint(registrationDto).andExpect(status().isBadRequest());

        RegistrationUserIdCourseIdDto registrationDto2 = new RegistrationUserIdCourseIdDto(1L, null);
        callEndpoint(registrationDto2).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 403 when ActionDeniedException is thrown")
    void shouldReturnActionDeniedWhenActionDeniedExceptionIsThrownTest() throws Exception {
        RegistrationUserIdCourseIdDto registrationDto = new RegistrationUserIdCourseIdDto(1L, 1L);

        Mockito.when(registrationService.save(registrationDto))
                .thenThrow(new ActionDeniedException("Course is not active"));

        callEndpoint(registrationDto).andExpect(status().isForbidden());
    }

    private ResultActions callEndpoint(RegistrationUserIdCourseIdDto registrationDto) throws Exception {
        String json = gson.toJson(registrationDto);

        return mockMvc.perform(MockMvcRequestBuilders
                .post(PATH)
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
