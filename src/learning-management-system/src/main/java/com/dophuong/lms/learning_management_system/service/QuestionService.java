package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Question;
import jakarta.validation.Valid;

import java.util.List;

public interface QuestionService {
    boolean existsQuestion(Long questionId);

    QuestionResponse getQuestion(Long courseId, Long questionId);

    QuestionResponse createQuestion(Long courseId, @Valid QuestionRequest request);

    List<QuestionResponse> getAllQuestion(Long courseId);
}
