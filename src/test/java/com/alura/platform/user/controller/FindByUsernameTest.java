package com.alura.platform.user.controller;

import com.alura.platform.user.dto.UserNameEmailRoleDto;
import com.alura.platform.user.enums.UserRoleEnum;
import com.alura.platform.user.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.webjars.NotFoundException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindByUsernameTest {

    private static final String PATH = "/user/by/username";

    private MockMvc mockMvc;
    private final Gson gson = new Gson();

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Should return user data when user was found")
    void shouldReturnUserDtoWhenUserWasFoundTest() throws Exception {
        String username = "joao";
        UserNameEmailRoleDto userDto = new UserNameEmailRoleDto("Joao Silva", "joao@gmail.com", UserRoleEnum.STUDENT);
        String expectedResponse = gson.toJson(userDto);

        Mockito.when(this.userService.findByUsername(username)).thenReturn(userDto);

        MvcResult result = callEndpoint(username).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();

        Mockito.verify(this.userService, Mockito.times(1)).findByUsername(username);

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return 400 when invalid username is received")
    void shouldReturnBadRequestWhenInvalidUsernameTest() throws Exception {
        String username = "";
        callEndpoint(username).andExpect(status().isBadRequest());

        username = null;
        callEndpoint(username).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when user was not found")
    void shouldReturnNotFoundWhenUserWasNotFoundTest() throws Exception {
        String username = "joao";

        Mockito.when(userService.findByUsername(username))
                .thenThrow(new NotFoundException("No user was found"));

        callEndpoint(username).andExpect(status().isNotFound());
    }

    private ResultActions callEndpoint(String username) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(PATH)
                .queryParam("username", username)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

}
