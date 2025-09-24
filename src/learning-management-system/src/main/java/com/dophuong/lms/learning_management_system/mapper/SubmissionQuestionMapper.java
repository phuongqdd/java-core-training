package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionQuestionResponse;
import com.dophuong.lms.learning_management_system.entity.SubmissionQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionQuestionMapper {
    @Mapping(source = "question.id", target = "questionId")
    SubmissionQuestionResponse toResponse(SubmissionQuestion question);

    List<SubmissionQuestionResponse> toResponseList(List<SubmissionQuestion> questionList);
}
