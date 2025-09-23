package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizExportResponse;

public interface QuizExportService {
    QuizExportResponse exportQuizToWord(QuizDetailResponse quizDetail);
}
