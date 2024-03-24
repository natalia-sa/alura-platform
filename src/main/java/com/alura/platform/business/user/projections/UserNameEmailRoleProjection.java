package com.alura.platform.business.user.projections;

import com.alura.platform.business.user.enums.UserRoleEnum;

public interface UserNameEmailRoleProjection {
    String getName();

    String getEmail();

    UserRoleEnum getRole();

}
