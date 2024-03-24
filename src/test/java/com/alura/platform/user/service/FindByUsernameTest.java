package com.alura.platform.user.service;

import com.alura.platform.user.dto.UserNameEmailRoleDto;
import com.alura.platform.user.entity.User;
import com.alura.platform.user.enums.UserRoleEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.webjars.NotFoundException;

@SpringBootTest
@ActiveProfiles({"test"})
class FindByUsernameTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsernameTest() {
        User user1 = new User("Joao Silva", "joao", "joao@gmail.com", "123", UserRoleEnum.STUDENT);
        User user2 = new User("Joana Silva", "joana", "joana@gmail.com", "123", UserRoleEnum.STUDENT);
        userService.save(user1);
        userService.save(user2);

        UserNameEmailRoleDto expectedUserDto = new UserNameEmailRoleDto(user2);

        UserNameEmailRoleDto userFound = userService.findByUsername("joana");

        Assertions.assertEquals(expectedUserDto, userFound);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user was not found")
    void shouldThrowExceptionWhenUserWasNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.findByUsername("ana"));
    }
}
