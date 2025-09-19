package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.QuestionHistoryResponse;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionHistoryMapper {
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "user.id", target = "userId")
    QuestionHistoryResponse toResponse(QuestionHistory history);
    List<QuestionHistoryResponse> toResponseList(List<QuestionHistory> histories);
}
