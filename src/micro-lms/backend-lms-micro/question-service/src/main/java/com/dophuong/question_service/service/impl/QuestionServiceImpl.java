package com.dophuong.question_service.service.impl;

import com.dophuong.question_service.dto.request.OptionRequest;
import com.dophuong.question_service.dto.request.OptionUpdateRequest;
import com.dophuong.question_service.dto.request.QuestionRequest;
import com.dophuong.question_service.dto.request.QuestionUpdateRequest;
import com.dophuong.question_service.dto.response.OptionResponse;
import com.dophuong.question_service.dto.response.QuestionOnlyResponse;
import com.dophuong.question_service.dto.response.QuestionResponse;
import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.entity.QuestionHistory;
import com.dophuong.question_service.enums.ActionType;
import com.dophuong.question_service.enums.Difficulty;
import com.dophuong.question_service.enums.ErrorCode;
import com.dophuong.question_service.exception.AppException;
import com.dophuong.question_service.mapper.QuestionMapper;
import com.dophuong.question_service.repository.QuestionRepository;
import com.dophuong.question_service.repository.feign.CourseClient;
import com.dophuong.question_service.service.OptionService;
import com.dophuong.question_service.service.QuestionHistoryService;
import com.dophuong.question_service.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final CourseClient courseClient;

    private final OptionService optionService;
    private final QuestionHistoryService questionHistoryService;

    private final QuestionMapper questionMapper;


    @Override
    public boolean existsQuestion(Long questionId) {
        return questionRepository.existsById(questionId);
    }

    @Override
    public QuestionResponse getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId);
        if(question == null)
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
        List<OptionResponse> optionResponseList= optionService.getAllOptionInQuestion(questionId);
        QuestionResponse response = questionMapper.toResponse(question);
        response.setOptions(optionResponseList);
        return response;
    }

    @Override
    public QuestionResponse getQuestion(Long courseId, Long questionId) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

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
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(request.getOptions() == null || request.getOptions().size() < 2){
            throw new AppException(ErrorCode.QUESTION_MINIMUM_CHOICES_REQUIRED);
        }

        Question question = questionMapper.toEntity(request);

        question = questionRepository.save(question, courseId);

        List<OptionResponse> optionResponseList = new ArrayList<>();

        for(OptionRequest request1 : request.getOptions()){
            OptionResponse optionResponse =  optionService.createOption(question.getId(), request1);
            optionResponseList.add(optionResponse);
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        QuestionHistory history = questionHistoryService.createHistory(userName, question, ActionType.CREATED.name());
        QuestionResponse response = questionMapper.toResponse(question);
        response.setOptions(optionResponseList);
        return response;
    }

    @Override
    public List<QuestionOnlyResponse> getAllQuestion(Long courseId) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        List<Question> questions = questionRepository.findAllByCourseId(courseId);
        return questionMapper.toQuestionOnlyResponses(questions);
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Long courseId, Long questionId, QuestionUpdateRequest request) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!existsQuestion(questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        Question question = questionMapper.toEntity1(request);
        question.setId(questionId);
        question = questionRepository.save(question, courseId);

        List<OptionResponse> optionResponseList = new ArrayList<>();

        for(OptionUpdateRequest request1 : request.getOptions()){
            OptionResponse optionResponse =  optionService.updateOption(request1, questionId);
            optionResponseList.add(optionResponse);
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        QuestionHistory history = questionHistoryService.createHistory(userName, question, ActionType.UPDATED.name());
        QuestionResponse response = questionMapper.toResponse(question);
        response.setOptions(optionResponseList);
        return response;
    }

    @Override
    @Transactional
    public void deleteQuestion(Long courseId, Long questionId) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!existsQuestion(questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        optionService.deleteByQuestionId(questionId);
        questionHistoryService.deleteByQuestionId(questionId);

        questionRepository.deleteById(questionId);
    }

    @Override
    public int getTotalQuestions(Long courseId) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        return questionRepository.countByCourseId(courseId);
    }

    @Override
    public Map<Difficulty, Integer> getQuestionsByDifficulty(Long courseId) {
        if(!Boolean.TRUE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        Map<Difficulty, Integer> map = new EnumMap<>(Difficulty.class);
        for(Difficulty difficulty : Difficulty.values()){
            int cnt = questionRepository.countByCourseIdAndDifficulty(courseId, difficulty);
            map.put(difficulty, cnt);
        }
        return map;
    }

    @Override
    public List<Long> getQuestionsByLevel(Long courseId, String level) {
        return questionRepository.getQuestionsByLevel(courseId, level);
    }

    @Override
    public String getLevelByQuestionId(Long questionId) {
        return questionRepository.getLevelByQuestionId(questionId);
    }

}
