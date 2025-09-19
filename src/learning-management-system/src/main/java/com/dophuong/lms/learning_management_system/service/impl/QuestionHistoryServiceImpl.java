package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.response.QuestionHistoryResponse;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ActionType;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.QuestionHistoryMapper;
import com.dophuong.lms.learning_management_system.repository.QuestionHistoryRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import com.dophuong.lms.learning_management_system.service.QuestionHistoryService;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import com.dophuong.lms.learning_management_system.service.UserService;
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
    private UserService userService;

    @Autowired
    @Lazy
    private QuestionService questionService;

    @Autowired
    @Lazy
    private CourseService courseService;

    @Autowired
    private QuestionHistoryMapper questionHistoryMapper;

    @Override
    public QuestionHistory createHistory(String username, Question question, String type) {
        User user = userService.getUserByUsername(username);

        QuestionHistory history = QuestionHistory.builder()
                .user(user)
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
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!questionService.existsQuestion(questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        List<QuestionHistory> questionHistories = questionHistoryRepository.findByQuestionId(questionId);
        List<QuestionHistoryResponse> responseList = questionHistoryMapper.toResponseList(questionHistories);
        for(QuestionHistoryResponse response : responseList){
            response.setUsername(userService.getUserById(response.getUserId()).getUsername());
            response.setTitle(questionService.getQuestion(response.getQuestionId()).getContent());
        }
        return responseList;
    }
}
