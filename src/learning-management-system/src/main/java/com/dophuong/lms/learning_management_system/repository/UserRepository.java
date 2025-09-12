package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String Email);
    boolean existsByPhone(String Phone);

    Optional<User> findByIdAndRole(Long Id, Role Role);
    List<User> findAllByRole(Role role);

    Optional<User> findByUsername(String userName);
}
