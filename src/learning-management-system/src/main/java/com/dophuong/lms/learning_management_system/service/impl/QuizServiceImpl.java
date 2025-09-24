package com.dophuong.lms.learning_management_system.service.impl;

import com.dophuong.lms.learning_management_system.dto.request.AddQuestionToQuizRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizCreateRequest;
import com.dophuong.lms.learning_management_system.dto.request.QuizUpdateRequest;
import com.dophuong.lms.learning_management_system.dto.response.QuestionResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizDetailResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizResponse;
import com.dophuong.lms.learning_management_system.dto.response.QuizSummaryResponse;
import com.dophuong.lms.learning_management_system.entity.Quiz;
import com.dophuong.lms.learning_management_system.entity.User;
import com.dophuong.lms.learning_management_system.entity.UserRole;
import com.dophuong.lms.learning_management_system.enums.Difficulty;
import com.dophuong.lms.learning_management_system.enums.ErrorCode;
import com.dophuong.lms.learning_management_system.enums.Role;
import com.dophuong.lms.learning_management_system.exception.AppException;
import com.dophuong.lms.learning_management_system.mapper.QuizMapper;
import com.dophuong.lms.learning_management_system.repository.QuizQuestionRepository;
import com.dophuong.lms.learning_management_system.repository.QuizRepository;
import com.dophuong.lms.learning_management_system.service.CourseService;
import com.dophuong.lms.learning_management_system.service.QuestionService;
import com.dophuong.lms.learning_management_system.service.QuizService;
import com.dophuong.lms.learning_management_system.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private CourseService courseService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Override
    @Transactional
    public QuizResponse createQuiz(Long courseId, QuizCreateRequest request) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        Quiz quiz = quizMapper.toEntity(request);

        if (request.getOpenTime() != null && request.getCloseTime() != null) {
            long availableMinutes = java.time.Duration.between(request.getOpenTime(), request.getCloseTime()).toMinutes();
            if (request.getTimeLimit() > availableMinutes) {
                throw new AppException(ErrorCode.TIME_LIMIT_EXCEEDS_CLOSE_TIME);
            }
        }

        List<Integer> totalLevel = validateToTalQuizRequest(quiz, courseId);

        quiz = quizRepository.createQuiz(courseId, quiz);

        User user = userService.getIdInLogin();

        quizRepository.createQuizRandomQuestion(quiz.getId(), user.getId(), totalLevel);

        return quizMapper.toResponse(quiz);
    }

    @Override
    public QuizDetailResponse getQuizDetail(Long courseId, Long quizId) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        Quiz quiz = quizRepository.findById(quizId);

        List<QuestionResponse> questionResponses = quizQuestionRepository.findTestDetailsByQuizId(quizId);

        QuizDetailResponse quizDetailResponse = quizMapper.toQuizDetailResponse(quiz);

        quizDetailResponse.setQuestionResponses(questionResponses);

        return quizDetailResponse;
    }

    @Override
    @Transactional
    public void deleteQuestionFromQuiz(Long courseId, Long quizId, Long questionId) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!questionService.existsQuestion(questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsQuestionInQuiz(quizId, questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        User user = userService.getIdInLogin();

        quizRepository.deleteQuestionInQuiz(courseId, quizId, questionId, user.getId());
    }

    @Override
    @Transactional
    public void addQuestionToQuiz(Long courseId, Long quizId, AddQuestionToQuizRequest request) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!questionService.existsQuestion(request.getQuestionId()))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        if(quizRepository.existsQuestionInQuiz(quizId, request.getQuestionId()))
            throw new AppException(ErrorCode.QUESTION_EXISTED);

        User user = userService.getIdInLogin();

        quizRepository.addQuestionToQuiz(quizId, user.getId(), request.getQuestionId());
    }

    @Override
    public void updateQuiz(Long courseId, Long quizId, QuizUpdateRequest request) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        User user = userService.getIdInLogin();
        Quiz quiz = quizMapper.toEntityUpdate(request);
        quizRepository.updateQuiz(quizId, user.getId(),quiz);
    }

    @Override
    public void deleteQuiz(Long courseId, Long quizId) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);
        quizRepository.deleteQuizById(quizId);
    }

    @Override
    public List<QuizSummaryResponse> getQuizzes(Long courseId) {
        if(!courseService.existsById(courseId))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        User user = userService.getIdInLogin();
        boolean isInstructorOrAdmin = false;
        boolean isStudent = false;
        for (UserRole ur : user.getUserRoles()) {
            String roleName = ur.getRole().getName();
            if ("ADMIN".equals(roleName) || "INSTRUCTOR".equals(roleName)) {
                isInstructorOrAdmin = true;
            }
            if ("STUDENT".equals(roleName)) {
                isStudent = true;
            }
        }
        List<Quiz> quizList = new ArrayList<>();
        if(isInstructorOrAdmin){
            quizList = quizRepository.findAllByCourseId(courseId);
        } else if (isStudent) {
            quizList = quizRepository.findAllByCourseIdForStudent(courseId);
        }

        List<QuizSummaryResponse> quizSummaryResponses = quizMapper.toQuizSummaryResponses(quizList);

        return quizSummaryResponses;
    }

    private List<Integer> validateToTalQuizRequest(Quiz quiz, Long courseId) {
        if(questionService.getTotalQuestions(courseId) < quiz.getTotal())
            throw new AppException(ErrorCode.INSUFFICIENT_QUESTION_BANK);

        if(quiz.getPctRl() == 0 && quiz.getPctUn() == 0 &&
                quiz.getPctAp() == 0 && quiz.getPctAn() == 0){
            quiz.setPctRl(30);
            quiz.setPctUn(40);
            quiz.setPctAp(20);
            quiz.setPctAn(10);
        }

        if(quiz.getPctRl() < 0 || quiz.getPctUn() < 0 ||
                quiz.getPctAp() < 0 || quiz.getPctAn() < 0)
            throw new AppException(ErrorCode.PERCENTAGE_NEGATIVE);

        int total = quiz.getPctRl() + quiz.getPctUn() + quiz.getPctAp() + quiz.getPctAn();
        if(total != 100)
            throw new AppException(ErrorCode.PERCENTAGE_NOT_100);

        Map<Difficulty, Integer> questionsByDifficulty = questionService.getQuestionsByDifficulty(courseId);

        int rlCount = Math.round(quiz.getTotal() * quiz.getPctRl() / 100.0f);
        int unCount = Math.round(quiz.getTotal() * quiz.getPctUn() / 100.0f);
        int apCount = Math.round(quiz.getTotal() * quiz.getPctAp() / 100.0f);
        int anCount = Math.round(quiz.getTotal() * quiz.getPctAn() / 100.0f);

        if(rlCount > questionsByDifficulty.get(Difficulty.EASY) ||
                unCount > questionsByDifficulty.get(Difficulty.MEDIUM) ||
                apCount > questionsByDifficulty.get(Difficulty.HARD) ||
                anCount > questionsByDifficulty.get(Difficulty.VERY_HARD))
            throw new AppException(ErrorCode.INSUFFICIENT_QUESTION_BANK);

        if(quiz.getPctAn() > 10)
            throw new AppException(ErrorCode.PERCENTAGE_AN_TOO_HIGH);

        // Trả về kết quả
        return List.of(rlCount, unCount, apCount, anCount);
    }

}
