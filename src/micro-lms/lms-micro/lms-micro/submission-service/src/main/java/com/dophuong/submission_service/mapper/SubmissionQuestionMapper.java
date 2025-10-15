package com.dophuong.submission_service.mapper;

import com.dophuong.submission_service.dto.response.SubmissionQuestionResponse;
import com.dophuong.submission_service.entity.SubmissionQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionQuestionMapper {
    SubmissionQuestionResponse toResponse(SubmissionQuestion question);

    List<SubmissionQuestionResponse> toResponseList(List<SubmissionQuestion> questionList);
}
