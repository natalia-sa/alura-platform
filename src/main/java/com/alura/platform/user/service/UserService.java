package com.alura.platform.user.service;

import com.alura.platform.user.dto.UserNameEmailRoleDto;
import com.alura.platform.user.entity.User;
import com.alura.platform.user.projections.UserNameEmailRoleProjection;
import com.alura.platform.user.repository.UserRepository;
import com.alura.platform.user.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(UserDto userDto) {
        User user = new User(userDto);
        return userRepository.save(user);
    }

    public UserNameEmailRoleDto findByUsername(String username) {
        UserNameEmailRoleProjection userProjection = userRepository.findByUsername(username);

        if (userProjection == null) {
            throw new NotFoundException("No user was found");
        }

        return new UserNameEmailRoleDto(userProjection);
    }
}
