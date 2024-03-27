package com.alura.platform.business.user.service;

import com.alura.platform.business.basic.service.BasicService;
import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends BasicService<User, Long> {

    User save(UserDto userDto);

    UserNameEmailRoleDto findNameEmailRoleByUsername(String username);

    UserDetails findByUsername(String username);
}
