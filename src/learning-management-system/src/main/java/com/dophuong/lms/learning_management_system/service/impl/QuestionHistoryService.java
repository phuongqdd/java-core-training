package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;

public interface QuestionHistoryService {
    QuestionHistory createHistory(String userName, Question question, String type);
}
