package com.dophuong.quiz_service.service;


import com.dophuong.quiz_service.dto.response.QuizDetailResponse;
import com.dophuong.quiz_service.dto.response.QuizExportResponse;

public interface QuizExportService {
    QuizExportResponse exportQuizToWord(QuizDetailResponse quizDetail);
}
