package com.alura.platform.business.user.service;

import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.projections.UserNameEmailRoleProjection;
import com.alura.platform.business.user.repository.UserRepository;
import com.alura.platform.exception.ActionDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Transactional
    @Override
    public User save(UserDto userDto) {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(userDto.username(), userDto.email());

        if (userOptional.isPresent()) {
            throw new ActionDeniedException("The chosen username or email is already in use");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(userDto.password());
        User user = new User(userDto, encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public UserNameEmailRoleDto findByUsername(String username) {
        UserNameEmailRoleProjection userProjection = userRepository.findNameEmailRoleByUsername(username);

        if (userProjection == null) {
            throw new NoSuchElementException("No user was found");
        }

        return new UserNameEmailRoleDto(userProjection);
    }
}
