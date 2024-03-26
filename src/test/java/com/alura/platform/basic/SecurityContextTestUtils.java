package com.alura.platform.basic;

import com.alura.platform.business.user.enums.UserRoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextTestUtils {

    public static void fakeAuthentication(UserRoleEnum role) {
        fakeAuthentication("fakeUserName", role);
    }

    public static void fakeAuthentication(String username, UserRoleEnum role) {
        com.alura.platform.business.user.entity.User user = new com.alura.platform.business.user.entity.User("name", username, "user@gmail.com", "pass", role);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
