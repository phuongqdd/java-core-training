package com.dophuong.question_service.mapper;

import com.dophuong.question_service.dto.request.QuestionRequest;
import com.dophuong.question_service.dto.request.QuestionUpdateRequest;
import com.dophuong.question_service.dto.response.QuestionOnlyResponse;
import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "options", source = "options")
    Question toEntity(QuestionRequest dto);

    @Mapping(target = "options", source = "options")
    Question toEntity1(QuestionUpdateRequest request);

    @Mapping(target = "options", source = "options")
    QuestionResponse toResponse(Question question);

    QuestionOnlyResponse toResponseOnly(Question question);

    List<QuestionResponse> toResponseList(List<Question> questions);

    List<QuestionOnlyResponse> toQuestionOnlyResponses(List<Question> questions);

    default Page<QuestionOnlyResponse> toQuestionOnlyResponses(Page<Question> page) {
        if (page == null || page.isEmpty()) {
            return Page.empty(page != null ? page.getPageable() : null);
        }
        List<QuestionOnlyResponse> content = toQuestionOnlyResponses(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}
