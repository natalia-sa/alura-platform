package com.alura.platform.business.user.service;

import com.alura.platform.business.basic.BasicService;
import com.alura.platform.business.user.dto.UserDto;
import com.alura.platform.business.user.dto.UserNameEmailRoleDto;
import com.alura.platform.business.user.entity.User;

public interface UserService extends BasicService<User, Long> {

    User save(UserDto userDto);

    UserNameEmailRoleDto findByUsername(String username);
}
