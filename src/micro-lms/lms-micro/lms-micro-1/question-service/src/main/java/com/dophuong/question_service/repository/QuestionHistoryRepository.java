package com.dophuong.question_service.repository;

import com.dophuong.question_service.entity.QuestionHistory;

import java.util.List;

public interface QuestionHistoryRepository {
    List<QuestionHistory> findByQuestionId(Long questionId);
    QuestionHistory save(QuestionHistory history);
    void deleteByQuestionId(Long questionId);
}
