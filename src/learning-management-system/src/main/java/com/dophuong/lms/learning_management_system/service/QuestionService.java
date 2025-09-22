package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuestionOnlyResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    boolean existsQuestion(Long questionId);

    Question getQuestion(Long questionId);

    QuestionResponse getQuestion(Long courseId, Long questionId);

    QuestionResponse createQuestion(Long courseId, @Valid QuestionRequest request);

    List<QuestionOnlyResponse> getAllQuestion(Long courseId);

    QuestionResponse updateQuestion(Long courseId, Long questionId, QuestionUpdateRequest request);

    void deleteQuestion(Long courseId, Long questionId);

    int getTotalQuestions(Long courseId);

    public Map<Difficulty, Integer> getQuestionsByDifficulty(Long courseId);
}
