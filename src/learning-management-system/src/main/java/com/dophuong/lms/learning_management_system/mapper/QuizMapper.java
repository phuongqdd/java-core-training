package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizResponse;
import com.dophuong.lms.learning_management_system.entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    @Mapping(source = "totalQuestions", target = "total")
    Quiz toEntity(QuizCreateRequest request);

    @Mapping(source = "course.name", target = "courseTitle")
    QuizResponse toResponse(Quiz quiz);

    List<QuizResponse> toResponseList (List<Quiz> list);

    @Mapping(source = "course.name", target = "courseTitle")
    QuizDetailResponse toQuizDetailResponse(Quiz quiz);

    Quiz toEntityUpdate(QuizUpdateRequest request);
}
