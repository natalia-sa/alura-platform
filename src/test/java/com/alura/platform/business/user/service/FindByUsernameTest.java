package com.alura.platform.business.user.service;

import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.enums.UserRoleEnum;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles({"test"})
class FindByUsernameTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should find user by username")
    @Transactional
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
    @Transactional
    void shouldThrowExceptionWhenUserWasNotFoundTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findByUsername("ana"));
    }
}
