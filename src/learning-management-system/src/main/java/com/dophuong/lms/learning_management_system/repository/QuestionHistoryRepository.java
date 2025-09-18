package com.dophuong.lms.learning_management_system.repository;

import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;

import java.util.List;

public interface QuestionHistoryRepository {
    List<QuestionHistory> findByQuestionId(Long questionId);
    QuestionHistory save(QuestionHistory history);
}
