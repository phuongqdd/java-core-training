package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
