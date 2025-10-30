package com.dophuong.submission_service.service.impl;

import com.dophuong.submission_service.dto.response.*;
import com.dophuong.submission_service.entity.Submission;
import com.dophuong.submission_service.enums.ErrorCode;
import com.dophuong.submission_service.exception.AppException;
import com.dophuong.submission_service.mapper.SubmissionMapper;
import com.dophuong.submission_service.repository.SubmissionRepository;
import com.dophuong.submission_service.repository.feign.CourseClient;
import com.dophuong.submission_service.repository.feign.QuizClient;
import com.dophuong.submission_service.repository.feign.UserClient;
import com.dophuong.submission_service.service.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX_QUIZ = "quiz:";
    private static final String CACHE_PREFIX_SUBMISSION = "submission:quiz:";

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CourseClient courseClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private QuizClient quizClient;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Override
    public SubmissionResponse startSubmission(Long courseId, Long quizId) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(Boolean.FALSE.equals(quizClient.existsQuizId(courseId, quizId).getBody()))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(Boolean.FALSE.equals(quizClient.existsByCourseIdAndQuizId(courseId, quizId).getBody()))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        UserResponse user = userClient.getUserInfo().getBody();
        Long userId = user.getId();

        String key = CACHE_PREFIX_SUBMISSION + quizId + ":user:" + userId;
        SubmissionResponse submissionResponse = (SubmissionResponse) redisTemplate.opsForValue().get(key);
        if(submissionResponse != null){
            log.warn("Đã vào đây hihi");
            return submissionResponse;
        }

        String key1 = CACHE_PREFIX_QUIZ + quizId;
        QuizDetailResponse quizDetai = (QuizDetailResponse) redisTemplate.opsForValue().get(key1);
        if(quizDetai != null && submissionResponse == null){
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra thời gian
            if (now.isBefore(quizDetai.getOpenTime()))
                throw new AppException(ErrorCode.SUBMISSION_NOT_OPEN_YET);
            if (now.isAfter(quizDetai.getCloseTime()))
                throw new AppException(ErrorCode.SUBMISSION_CLOSED);

            // Kiểm tra số lần làm còn lại
            int soLanDaLam = submissionRepository.countQuizAttempts(userId, quizId);
            int soLanLamToiDa = quizDetai.getAttemptsAllowed();

            if(soLanDaLam >= soLanLamToiDa)
                throw new AppException(ErrorCode.SUBMISSION_OUT_OF_ATTEMPTS);

            List<QuestionResponse> questionResponseList = quizDetai.getQuestionResponses();
            int atp= submissionRepository.countQuizAttempts(userId, quizId) + 1;
            SubmissionResponse response =  submissionRepository.daoDe(quizId, userId, atp, courseId, questionResponseList);
            luuCacheUpdate(response, key);
        }

        // Lấy quiz time và attempts
        QuizResponse quizTime = quizClient.getQuiz(courseId, quizId).getBody(); // open_time, close_time, time_limit
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra thời gian
        if (now.isBefore(quizTime.getOpenTime()))
            throw new AppException(ErrorCode.SUBMISSION_NOT_OPEN_YET);
        if (now.isAfter(quizTime.getCloseTime()))
            throw new AppException(ErrorCode.SUBMISSION_CLOSED);

        // Kiểm tra submission đang làm (IN_PROGRESS)
        Submission existing = submissionRepository.getInProgressSubmission(userId, quizId);
        if (existing != null) {
            // Kiểm tra đã hết thời gian chưa
            LocalDateTime finishTime = existing.getStartedAt().plusMinutes(quizTime.getTimeLimit());
            if (now.isAfter(finishTime)) {
                // Tự động kết thúc submission
                submissionRepository.finishSubmission(courseId, quizId, existing.getSubmissionId());
            } else {
                return submissionRepository.traKetQua(courseId, quizId, existing.getSubmissionId());
            }
        }

        // Kiểm tra số lần làm còn lại
        int soLanDaLam = submissionRepository.countQuizAttempts(userId, quizId);
        int soLanLamToiDa = quizClient.getAttempts(courseId, quizId).getBody();

        if(soLanDaLam >= soLanLamToiDa)
            throw new AppException(ErrorCode.SUBMISSION_OUT_OF_ATTEMPTS);

        SubmissionResponse response =  submissionRepository.createSubmission(courseId, quizId, userId);
        luuCacheUpdate(response, key);
        return response;
    }

    private void luuCacheUpdate(SubmissionResponse response, String key) {
        redisTemplate.opsForValue().set(
                key,
                response,
                Duration.ofMinutes(response.getTimeLimit()) // chuyển int -> Duration
        );
    }


}
