package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionOnlyResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true) // course set ở service
    @Mapping(target = "options", source = "options")
    Question toEntity(QuestionRequest dto);

    @Mapping(target = "options", source = "options")
    Question toEntity1(QuestionUpdateRequest request);

    @Mapping(target = "options", source = "options")
    QuestionResponse toResponse(Question question);

    QuestionOnlyResponse toResponseOnly(Question question);

    List<QuestionResponse> toResponseList(List<Question> questions);

    List<QuestionOnlyResponse> toQuestionOnlyResponses(List<Question> questions);
}
