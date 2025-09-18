package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.OptionRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuestionRequest;
import com.dophuong.lms.learning_management_system.dto.response.OptionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;
import com.dophuong.lms.learning_management_system.enums.ActionType;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.QuestionMapper;
import com.dophuong.lms.learning_management_system.repository.CourseJdbcRepository;
import com.dophuong.lms.learning_management_system.repository.QuestionRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import com.dophuong.lms.learning_management_system.service.OptionService;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CourseJdbcRepository courseJdbcRepository;

    private final CourseService courseService;
    private final OptionService optionService;
    private final QuestionHistoryService questionHistoryService;
    private final QuestionMapper questionMapper;


    @Override
    public boolean existsQuestion(Long questionId) {
        return questionRepository.existsById(questionId);
    }

    @Override
    public QuestionResponse getQuestion(Long courseId, Long questionId) {
        Question question = questionRepository.findById(questionId);
        if(question == null)
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
        List<OptionResponse> optionResponseList= optionService.getAllOptionInQuestion(questionId);
        QuestionResponse response = questionMapper.toResponse(question);
        response.setOptions(optionResponseList);
        return response;
    }

    @Override
    @Transactional
    public QuestionResponse createQuestion(Long courseId, QuestionRequest request) {
        Course course = courseService.getCourse(courseId);

        if(request.getOptions() == null || request.getOptions().size() < 2){
            throw new AppException(ErrorCode.QUESTION_MINIMUM_CHOICES_REQUIRED);
        }

        Question question = questionMapper.toEntity(request);

        question = questionRepository.save(question, courseId);

        List<OptionResponse> optionResponseList = new ArrayList<>();

        for(OptionRequest request1 : request.getOptions()){
            log.warn("COn đin 1: " + request1.isCorrect());
            OptionResponse optionResponse =  optionService.createOption(question.getId(), request1);
            log.warn("Con diên 3: " + optionResponse.getId());
            optionResponseList.add(optionResponse);
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        QuestionHistory history = questionHistoryService.createHistory(userName, question, ActionType.CREATED.name());
        QuestionResponse response = questionMapper.toResponse(question);
        response.setOptions(optionResponseList);
        return response;
    }

    @Override
    public List<QuestionResponse> getAllQuestion(Long courseId) {
        
        return List.of();
    }
}
