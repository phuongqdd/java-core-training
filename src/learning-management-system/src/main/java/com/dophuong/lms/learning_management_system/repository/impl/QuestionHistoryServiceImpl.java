package com.dophuong.lms.learning_management_system.repository.impl;

import com.dophuong.lms.learning_management_system.entity.Course;
import com.dophuong.lms.learning_management_system.entity.Question;
import com.dophuong.lms.learning_management_system.entity.QuestionHistory;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ActionType;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.repository.QuestionHistoryRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import com.dophuong.lms.learning_management_system.service.UserService;
import com.dophuong.lms.learning_management_system.service.impl.QuestionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionHistoryServiceImpl implements QuestionHistoryService {

    @Autowired
    private QuestionHistoryRepository questionHistoryRepository;

    @Autowired
    private UserService userService;

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
}
