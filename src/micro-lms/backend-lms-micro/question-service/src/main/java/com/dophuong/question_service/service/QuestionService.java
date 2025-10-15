package com.dophuong.question_service.service;

import com.dophuong.question_service.dto.request.QuestionRequest;
import com.dophuong.question_service.dto.request.QuestionUpdateRequest;
import com.dophuong.question_service.dto.response.QuestionOnlyResponse;
import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.enums.Difficulty;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    boolean existsQuestion(Long questionId);

    QuestionResponse getQuestion(Long questionId);

    QuestionResponse getQuestion(Long courseId, Long questionId);

    QuestionResponse createQuestion(Long courseId, QuestionRequest request);

    List<QuestionOnlyResponse> getAllQuestion(Long courseId);

    QuestionResponse updateQuestion(Long courseId, Long questionId, QuestionUpdateRequest request);

    void deleteQuestion(Long courseId, Long questionId);

    int getTotalQuestions(Long courseId);

    Map<Difficulty, Integer> getQuestionsByDifficulty(Long courseId);

    List<Long> getQuestionsByLevel(Long courseId, String level);

    String getLevelByQuestionId(Long questionId);
}
