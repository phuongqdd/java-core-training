package com.dophuong.question_service.service.impl;

import com.dophuong.question_service.dto.response.QuestionHistoryResponse;
import com.dophuong.question_service.dto.response.UserResponse;
import com.dophuong.question_service.entity.Question;
import com.dophuong.question_service.entity.QuestionHistory;
import com.dophuong.question_service.enums.ActionType;
import com.dophuong.question_service.enums.ErrorCode;
import com.dophuong.question_service.exception.AppException;
import com.dophuong.question_service.mapper.QuestionHistoryMapper;
import com.dophuong.question_service.repository.QuestionHistoryRepository;
import com.dophuong.question_service.repository.feign.UserClient;
import com.dophuong.question_service.service.QuestionHistoryService;
import com.dophuong.question_service.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class QuestionHistoryServiceImpl implements QuestionHistoryService {

    @Autowired
    private QuestionHistoryRepository questionHistoryRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    @Lazy
    private QuestionService questionService;

    @Autowired
    private QuestionHistoryMapper questionHistoryMapper;

    @Override
    public QuestionHistory createHistory(String username, Question question, String type) {
        UserResponse userResponse = userClient.getByUsername(username).getBody();
        Long userId = userResponse.getId();
        QuestionHistory history = QuestionHistory.builder()
                .userId(userId)
                .question(question)
                .actionType(type == ActionType.CREATED.name() ? ActionType.CREATED : ActionType.UPDATED)
                .time(LocalDateTime.now())
                .build();
        return questionHistoryRepository.save(history);
    }

    @Override
    public void deleteByQuestionId(Long questionId) {
        questionHistoryRepository.deleteByQuestionId(questionId);
    }

    @Override
    public List<QuestionHistoryResponse> getHistoryByQuestionId(Long questionId, Long courseId) {
//        if(!courseService.existsById(courseId))
//            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!questionService.existsQuestion(questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        List<QuestionHistory> questionHistories = questionHistoryRepository.findByQuestionId(questionId);
        List<QuestionHistoryResponse> responseList = questionHistoryMapper.toResponseList(questionHistories);
        for(QuestionHistoryResponse response : responseList){
//            response.setUsername(userService.getUserById(response.getUserId()).getUsername());
            response.setTitle(questionService.getQuestion(response.getQuestionId()).getContent());
        }
        return responseList;
    }
}
