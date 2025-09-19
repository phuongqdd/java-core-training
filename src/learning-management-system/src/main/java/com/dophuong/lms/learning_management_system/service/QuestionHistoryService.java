package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.response.QuestionHistoryResponse;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;

import java.util.List;

public interface QuestionHistoryService {
    QuestionHistory createHistory(String userName, Question question, String type);
    void deleteByQuestionId(Long questionId);

    List<QuestionHistoryResponse> getHistoryByQuestionId(Long questionId, Long courseId);
}
