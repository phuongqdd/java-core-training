package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.entity.Option;
import com.dophuong.lms.learning_management_system.mapper.OptionMapper;
import com.dophuong.lms.learning_management_system.repository.OptionRepository;
import com.dophuong.lms.learning_management_system.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private OptionMapper optionMapper;

    @Override
    public OptionResponse createOption(Long id, OptionRequest request) {
        Option option = optionMapper.toEntity(request);
        option = optionRepository.save(option, id);
        return optionMapper.toResponse(option);
    }

    @Override
    public List<OptionResponse> getAllOptionInQuestion(Long questionId) {
        List<Option> options = optionRepository.findByQuestionId(questionId);
        return optionMapper.toResponseList(options);
    }
}
