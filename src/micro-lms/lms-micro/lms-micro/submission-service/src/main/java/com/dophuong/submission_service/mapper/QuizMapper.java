package com.dophuong.submission_service.mapper;

import com.dophuong.submission_service.dto.response.QuizDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    QuizDetailResponse toResponse(com.example.common_service.dto.response.QuizDetailResponse quizDetailResponse);
}
