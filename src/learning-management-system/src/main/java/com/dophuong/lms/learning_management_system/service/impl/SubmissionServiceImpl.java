package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.response.SubmissionResponse;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.repository.QuizRepository;
import com.dophuong.lms.learning_management_system.repository.SubmissionRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import com.dophuong.lms.learning_management_system.service.SubmissionService;
import com.dophuong.lms.learning_management_system.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @Override
    public SubmissionResponse startSubmission(Long courseId, Long quizId) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        User user = userService.getIdInLogin();
        Long userId = user.getId();

        int soLanDaLam = submissionRepository.countQuizAttempts(userId, quizId);
        int soLanLamToiDa = quizRepository.findAttemptsById(quizId);

        if(soLanDaLam >= soLanLamToiDa)
            throw new AppException(ErrorCode.SUBMISSION_OUT_OF_ATTEMPTS);



        return submissionRepository.createSubmission(quizId, userId);
    }

    public void validateQuizTime(Long quizId) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime openTime = quizRepository.findOpenTimeById(quizId);
        LocalDateTime closeTime = quizRepository.findCloseTimeById(quizId);

        if (openTime != null && now.isBefore(openTime)) {
            throw new AppException(ErrorCode.SUBMISSION_NOT_OPEN_YET);
        }

        if (closeTime != null && now.isAfter(closeTime)) {
            throw new AppException(ErrorCode.SUBMISSION_CLOSED);
        }
    }
}
