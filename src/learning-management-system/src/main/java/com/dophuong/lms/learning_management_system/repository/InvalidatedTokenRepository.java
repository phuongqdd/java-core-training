package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
