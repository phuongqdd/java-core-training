package com.dophuong.question_service.service;

import com.dophuong.question_service.dto.response.QuestionHistoryResponse;
import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.entity.QuestionHistory;

import java.util.List;

public interface QuestionHistoryService {
    QuestionHistory createHistory(String userName, Question question, String type);
    void deleteByQuestionId(Long questionId);

    List<QuestionHistoryResponse> getHistoryByQuestionId(Long questionId, Long courseId);
}
