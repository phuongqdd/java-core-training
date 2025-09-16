package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.Role;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String Email);
    boolean existsByPhone(String Phone);

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.name = :roleName")
    List<User> findAllByRole(@Param("roleName") String roleName);

    Optional<User> findByUsername(String userName);


    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role " +
            "WHERE u.username = :username"
    )
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
