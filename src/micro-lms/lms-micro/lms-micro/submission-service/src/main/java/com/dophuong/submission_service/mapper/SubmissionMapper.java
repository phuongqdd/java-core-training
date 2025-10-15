package com.dophuong.submission_service.mapper;

import com.dophuong.submission_service.dto.response.SubmissionResponse;
import com.dophuong.submission_service.entity.Submission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    SubmissionResponse toResponse(Submission submission);
}
