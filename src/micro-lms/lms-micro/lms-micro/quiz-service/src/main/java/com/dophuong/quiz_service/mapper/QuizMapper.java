package com.dophuong.quiz_service.mapper;

import com.dophuong.quiz_service.dto.request.QuizCreateRequest;
import com.dophuong.quiz_service.dto.request.QuizUpdateRequest;
import com.dophuong.quiz_service.dto.response.QuizDetailResponse;
import com.dophuong.quiz_service.dto.response.QuizResponse;
import com.dophuong.quiz_service.dto.response.QuizSummaryResponse;
import com.dophuong.quiz_service.entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    @Mapping(source = "totalQuestions", target = "total")
    Quiz toEntity(QuizCreateRequest request);

    QuizResponse toResponse(Quiz quiz);

    List<QuizResponse> toResponseList (List<Quiz> list);

    QuizDetailResponse toQuizDetailResponse(Quiz quiz);

    Quiz toEntityUpdate(QuizUpdateRequest request);

    @Mapping(source = "published", target = "published")
    QuizSummaryResponse toQuizSummaryResponse(Quiz quiz);

    List<QuizSummaryResponse> toQuizSummaryResponses(List<Quiz> list);

    com.example.common_service.dto.response.QuizDetailResponse toResponseDetail(QuizDetailResponse quizDetailResponse);

    QuizDetailResponse toResponseDetailQS(com.example.common_service.dto.response.QuizDetailResponse quizDetailResponse);
}
