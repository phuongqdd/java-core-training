package com.dophuong.lms.learning_management_system.mapper;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true) // course set ở service
    @Mapping(target = "options", source = "options")
    Question toEntity(QuestionRequest dto);

    Option toEntity(OptionRequest dto);

    // Entity → Response
    QuestionResponse toResponse(Question question);

    OptionResponse toResponse(Option option);
}
