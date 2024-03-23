package com.alura.platform.user;

import com.alura.platform.user.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(UserDto userDto) {
        User user = new User(userDto);
        return userRepository.save(user);
    }
}
