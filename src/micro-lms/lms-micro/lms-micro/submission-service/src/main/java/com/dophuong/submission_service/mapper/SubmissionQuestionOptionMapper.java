package com.dophuong.submission_service.mapper;

import com.dophuong.submission_service.dto.response.OptionReviewResponse;
import com.dophuong.submission_service.dto.response.SubmissionQuestionOptionResponse;
import com.dophuong.submission_service.entity.SubmissionQuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionQuestionOptionMapper {
    SubmissionQuestionOptionResponse toResponse(SubmissionQuestionOption submissionQuestionOption);

    List<SubmissionQuestionOptionResponse> toResponseList(List<SubmissionQuestionOption> list);

    OptionReviewResponse toReviewResponse(SubmissionQuestionOption submissionQuestionOption);

    List<OptionReviewResponse> toReviewResponseList(List<SubmissionQuestionOption> list);
}
