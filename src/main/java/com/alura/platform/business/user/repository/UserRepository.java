package com.alura.platform.business.user.repository;

import com.alura.platform.business.user.entity.User;
import com.alura.platform.business.user.projections.UserNameEmailRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(@Param("username") String username);

    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("""
            SELECT user.name AS name,
                   user.email AS email,
                   user.role AS role
            FROM User user
            WHERE user.username = :username""" )
    UserNameEmailRoleProjection findNameEmailRoleByUsername(@Param("username") String username);
}
