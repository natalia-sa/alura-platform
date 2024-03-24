package com.alura.platform.business.user.repository;

import com.alura.platform.business.user.projections.UserNameEmailRoleProjection;
import com.alura.platform.business.user.entity.User;
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
