package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
}
