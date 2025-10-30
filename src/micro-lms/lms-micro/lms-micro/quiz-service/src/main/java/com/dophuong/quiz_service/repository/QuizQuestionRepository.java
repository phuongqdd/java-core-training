package com.dophuong.quiz_service.repository;


import com.dophuong.quiz_service.dto.response.QuestionResponse;

import java.util.List;

public interface QuizQuestionRepository {
    List<QuestionResponse> findTestDetailsByQuizId(Long courseId, Long quizId);

    void save(Long quizId, Long questionId);

    List<Long> findQuestionIdsByQuizId(Long quizId);
}
