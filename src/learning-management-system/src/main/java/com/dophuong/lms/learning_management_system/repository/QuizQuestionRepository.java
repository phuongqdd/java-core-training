package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;

import java.util.List;

public interface QuizQuestionRepository {
    List<QuestionResponse> findTestDetailsByQuizId(Long quizId);
}
