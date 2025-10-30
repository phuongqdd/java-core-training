package com.dophuong.question_service.service;

import com.dophuong.question_service.dto.request.QuestionRequest;
import com.dophuong.question_service.dto.request.QuestionUpdateRequest;
import com.dophuong.question_service.dto.response.QuestionOnlyResponse;
import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    boolean existsQuestion(Long questionId);

    QuestionResponse getQuestion(Long questionId);

    QuestionResponse getQuestion(Long courseId, Long questionId);

    QuestionResponse createQuestion(Long courseId, QuestionRequest request);

    Page<QuestionOnlyResponse> getAllQuestion(Long courseId, Pageable pageable);

    QuestionResponse updateQuestion(Long courseId, Long questionId, QuestionUpdateRequest request);

    void deleteQuestion(Long courseId, Long questionId);

    int getTotalQuestions(Long courseId);

    Map<Difficulty, Integer> getQuestionsByDifficulty(Long courseId);

    List<Long> getQuestionsByLevel(Long courseId, String level);

    String getLevelByQuestionId(Long questionId);
}
