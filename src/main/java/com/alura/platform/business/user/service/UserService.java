package com.alura.platform.business.user.service;

import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.projections.UserNameEmailRoleProjection;
import com.alura.platform.business.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(UserDto userDto) {
        User user = new User(userDto);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public UserNameEmailRoleDto findByUsername(String username) {
        UserNameEmailRoleProjection userProjection = userRepository.findByUsername(username);

        if (userProjection == null) {
            throw new NotFoundException("No user was found");
        }

        return new UserNameEmailRoleDto(userProjection);
    }
}
