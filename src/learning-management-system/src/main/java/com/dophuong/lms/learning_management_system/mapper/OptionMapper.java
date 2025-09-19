package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.OptionUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    Option toEntity(OptionRequest request);
    Option toEntity1(OptionUpdateRequest request);
    OptionResponse toResponse(Option option);
    List<OptionResponse> toResponseList(List<Option> options);
}
