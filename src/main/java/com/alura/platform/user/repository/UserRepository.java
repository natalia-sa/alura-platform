package com.alura.platform.user.repository;

import com.alura.platform.user.entity.User;
import com.alura.platform.user.projections.UserNameEmailRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT user.name AS name,
                   user.email AS email,
                   user.role AS role
            FROM User user
            WHERE user.username = :username""" )
    UserNameEmailRoleProjection findByUsername(@Param("username") String username);
}
