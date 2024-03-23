package com.alura.platform.user.service;

import com.alura.platform.user.dto.UserDto;
import com.alura.platform.user.entity.User;
import com.alura.platform.user.enums.UserRoleEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test"})
class FindByUsernameTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserTest() {
        UserDto userDto = makeUserDto(
                "joao",
                "joao",
                "joao.silva_12@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        User user = userService.save(userDto);
        UserDto savedUserDto = new UserDto(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(1, userService.findAll().size());
        Assertions.assertEquals(userDto, savedUserDto);
    }

    private UserDto makeUserDto(String name, String username, String email, String password, UserRoleEnum role) {
        return new UserDto(name, username, email, password, role);
    }
}
