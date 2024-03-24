package com.alura.platform.business.user.controller;

import com.alura.platform.basic.BasicControllerTest;
import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.business.user.service.UserService;
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

    private static final String PATH = "/user";

    private final Gson gson = new Gson();

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Should return 201 when user was created with success")
    void shouldReturnSuccessWhenUserWasCreatedSuccessfullyTest() throws Exception {
        UserDto userDto = makeUserDto(
                "joao",
                "joao",
                "joao.silva_12@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        User user = new User(userDto);

        Mockito.when(this.userService.save(userDto)).thenReturn(user);

        callEndpoint(userDto).andExpect(status().isCreated());

        Mockito.verify(this.userService, Mockito.times(1)).save(userDto);
    }

    @ParameterizedTest
    @MethodSource(value = "returnInvalidEmail")
    @DisplayName("Should return 400 when invalid email is received")
    void shouldReturnBadRequestWhenEmailIsInvalid(String email) throws Exception {
        UserDto userDto = makeUserDto(
                "joao",
                "joao",
                email,
                "123",
                UserRoleEnum.STUDENT);

        callEndpoint(userDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnInvalidEmail() {
        return Stream.of(
                Arguments.of("joaogmail.com"),
                Arguments.of("joao@gmail"),
                Arguments.of("joao@gmail.c"),
                Arguments.of("jo.ao@gmailc")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "returnInvalidUsername")
    @DisplayName("Should return 400 when invalid username is received")
    void shouldReturnBadRequestWhenInvalidUsernameTest(String username) throws Exception {
        UserDto userDto = makeUserDto(
                "Joao silva",
                username,
                "joao@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        callEndpoint(userDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnInvalidUsername() {
        return Stream.of(
                Arguments.of("joao1"),
                Arguments.of("joao silva"),
                Arguments.of("Joao"),
                Arguments.of("joaosilvacabralsoutojunior")
        );
    }

    @ParameterizedTest
    @MethodSource(value = "returnBlankParam")
    @DisplayName("Should return 400 when blank param is received")
    void shouldReturnBadRequestWhenParamIsBlankTest(
            String name, String username, String email, String password, UserRoleEnum role) throws Exception {

        UserDto userDto = makeUserDto(name, username, email, password, role);
        callEndpoint(userDto).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> returnBlankParam() {
        return Stream.of(
                Arguments.of("", "joao", "joao@gmail.com", "123", UserRoleEnum.STUDENT),
                Arguments.of("Joao Silva", "", "joao@gmail.com", "123", UserRoleEnum.STUDENT),
                Arguments.of("Joao Silva", "joao", "", "123", UserRoleEnum.STUDENT),
                Arguments.of("Joao Silva", "joao", "joao@gmail.com", "", UserRoleEnum.STUDENT)
        );
    }

    private ResultActions callEndpoint(UserDto userDto) throws Exception {
        String json = gson.toJson(userDto);

        return mockMvc.perform(MockMvcRequestBuilders
                .post(PATH)
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    private UserDto makeUserDto(String name, String username, String email, String password, UserRoleEnum role) {
        return new UserDto(name, username, email, password, role);
    }
}
