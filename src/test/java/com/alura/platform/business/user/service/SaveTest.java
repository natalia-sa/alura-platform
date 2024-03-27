package com.alura.platform.business.user.service;

import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test"})
class SaveTest {

    @Autowired
    private UserServiceImpl userService;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        userService.deleteAll();
    }

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

        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(userDto.username(), user.getUsername());
        Assertions.assertEquals(1, userService.findAll().size());
    }

    @Test
    @DisplayName("Should throw ActionDeniedException when trying to insert username or email that already exists in database")
    void shouldThrowActionDeniedExceptionTest() {
        UserDto userDto1 = makeUserDto(
                "joao",
                "joao",
                "joao.silva_12@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        userService.save(userDto1);

        UserDto userDtoWithSameEmail = makeUserDto(
                "joao",
                "joaoo",
                "joao.silva_12@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        UserDto userDtoWithSameUsername = makeUserDto(
                "joao",
                "joao",
                "joao.si_12@gmail.com",
                "123",
                UserRoleEnum.STUDENT);

        Assertions.assertThrows(ActionDeniedException.class, () -> userService.save(userDtoWithSameEmail));
        Assertions.assertThrows(ActionDeniedException.class, () -> userService.save(userDtoWithSameUsername));
    }

    private UserDto makeUserDto(String name, String username, String email, String password, UserRoleEnum role) {
        return new UserDto(name, username, email, password, role);
    }
}
