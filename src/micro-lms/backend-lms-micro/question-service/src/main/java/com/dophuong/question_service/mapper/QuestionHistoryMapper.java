package com.dophuong.question_service.mapper;

import com.dophuong.question_service.dto.response.QuestionHistoryResponse;
import com.dophuong.question_service.entity.QuestionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionHistoryMapper {
    @Mapping(source = "question.id", target = "questionId")
    QuestionHistoryResponse toResponse(QuestionHistory history);
    List<QuestionHistoryResponse> toResponseList(List<QuestionHistory> histories);
}
