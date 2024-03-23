package com.alura.platform.user.projections;

import com.alura.platform.user.enums.UserRoleEnum;

public interface UserNameEmailRoleProjection {
    String getName();

    String getEmail();

    UserRoleEnum getRole();

}
