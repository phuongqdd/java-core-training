package com.dophuong.question_service.mapper;

import com.dophuong.question_service.dto.request.OptionRequest;
import com.dophuong.question_service.dto.request.OptionUpdateRequest;
import com.dophuong.question_service.dto.response.OptionResponse;
import com.dophuong.question_service.entity.Option;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    Option toEntity(OptionRequest request);
    Option toEntity1(OptionUpdateRequest request);
    OptionResponse toResponse(Option option);
    List<OptionResponse> toResponseList(List<Option> options);
}
