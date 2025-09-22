package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizResponse;

public interface QuizService {
    QuizResponse createQuiz(Long courseId, QuizCreateRequest request);

    QuizDetailResponse getQuizDetail(Long courseId, Long quizId);
}
