package com.dophuong.question_service.service;


import com.dophuong.question_service.dto.request.OptionRequest;
import com.dophuong.question_service.dto.request.OptionUpdateRequest;
import com.dophuong.question_service.dto.response.OptionResponse;

import java.util.List;

public interface OptionService {
    OptionResponse createOption(Long id, OptionRequest request);

    List<OptionResponse> getAllOptionInQuestion(Long questionId);

    boolean existsById(int optionId);

    void deleteOption(int optionId);

    void deleteByQuestionId(Long questionsId);

    OptionResponse updateOption(OptionUpdateRequest request, Long questionId);
}
