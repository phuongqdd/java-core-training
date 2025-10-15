package com.dophuong.quiz_service.service;


import com.dophuong.quiz_service.dto.request.AddQuestionToQuizRequest;
import com.dophuong.quiz_service.dto.request.QuizCreateRequest;
import com.dophuong.quiz_service.dto.request.QuizUpdateRequest;
import com.dophuong.quiz_service.dto.response.QuestionResponse;
import com.dophuong.quiz_service.dto.response.QuizDetailResponse;
import com.dophuong.quiz_service.dto.response.QuizResponse;
import com.dophuong.quiz_service.dto.response.QuizSummaryResponse;

import java.util.List;

public interface QuizService {
    Boolean exists(Long quizId);
    
    Boolean existsByCourseIdAndQuizId(Long courseId, Long quizId);

    QuizResponse createQuiz(Long courseId, QuizCreateRequest request);

    QuizDetailResponse getQuizDetail(Long courseId, Long quizId);

    void deleteQuestionFromQuiz(Long courseId, Long quizId, Long questionId);

    void addQuestionToQuiz(Long courseId, Long quizId, AddQuestionToQuizRequest request);

    void updateQuiz(Long courseId, Long quizId, QuizUpdateRequest request);

    void deleteQuiz(Long courseId, Long quizId);

    List<QuizSummaryResponse> getQuizzes(Long courseId);

    Integer getAttempts(Long quizId);

    List<QuestionResponse> getAllQuestionDetailByQuizId(Long courseId, Long quizId);

    QuizResponse getQuiz(Long quizId);
}
