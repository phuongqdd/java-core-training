package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.AddQuestionToQuizRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizSummaryResponse;

import java.util.List;

public interface QuizService {
    QuizResponse createQuiz(Long courseId, QuizCreateRequest request);

    QuizDetailResponse getQuizDetail(Long courseId, Long quizId);

    void deleteQuestionFromQuiz(Long courseId, Long quizId, Long questionId);

    void addQuestionToQuiz(Long courseId, Long quizId, AddQuestionToQuizRequest request);

    void updateQuiz(Long courseId, Long quizId, QuizUpdateRequest request);

    void deleteQuiz(Long courseId, Long quizId);

    List<QuizSummaryResponse> getQuizzes(Long courseId);
}
