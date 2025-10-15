package com.dophuong.quiz_service.repository;

import com.dophuong.quiz_service.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
}
