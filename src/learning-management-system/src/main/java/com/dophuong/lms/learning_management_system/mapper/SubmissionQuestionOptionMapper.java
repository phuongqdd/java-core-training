package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionQuestionOptionResponse;
import com.dophuong.lms.learning_management_system.entity.SubmissionQuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionQuestionOptionMapper {
    @Mapping(source = "option.id", target = "optionId")
    SubmissionQuestionOptionResponse toResponse(SubmissionQuestionOption submissionQuestionOption);

    List<SubmissionQuestionOptionResponse> toResponseList(List<SubmissionQuestionOption> list);
}
