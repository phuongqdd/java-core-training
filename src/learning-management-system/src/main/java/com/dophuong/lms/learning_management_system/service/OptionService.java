package com.dophuong.lms.learning_management_system.service;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.OptionUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;

import java.util.List;

public interface OptionService {
    OptionResponse createOption(Long id, OptionRequest request);

    List<OptionResponse> getAllOptionInQuestion(Long questionId);

    boolean existsById(int optionId);

    void deleteOption(int optionId);

    void deleteByQuestionId(Long questionsId);

    OptionResponse updateOption(OptionUpdateRequest request, Long questionId);
}
