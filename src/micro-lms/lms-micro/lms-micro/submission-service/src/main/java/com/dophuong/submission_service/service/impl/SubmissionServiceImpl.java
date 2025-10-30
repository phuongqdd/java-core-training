package com.dophuong.submission_service.service.impl;

import com.dophuong.submission_service.dto.request.AnswerRequest;
import com.dophuong.submission_service.dto.request.SubmissionRequest;
import com.dophuong.submission_service.dto.response.*;
import com.dophuong.submission_service.entity.Submission;
import com.dophuong.submission_service.entity.SubmissionQuestion;
import com.dophuong.submission_service.entity.SubmissionQuestionOption;
import com.dophuong.submission_service.enums.ErrorCode;
import com.dophuong.submission_service.exception.AppException;
import com.dophuong.submission_service.mapper.QuizMapper;
import com.dophuong.submission_service.mapper.SubmissionMapper;
import com.dophuong.submission_service.mapper.SubmissionQuestionMapper;
import com.dophuong.submission_service.mapper.SubmissionQuestionOptionMapper;
import com.dophuong.submission_service.repository.SubmissionQuestionOptionRepository;
import com.dophuong.submission_service.repository.SubmissionQuestionRepository;
import com.dophuong.submission_service.repository.SubmissionRepository;
import com.dophuong.submission_service.repository.feign.CourseClient;
import com.dophuong.submission_service.repository.feign.QuizClient;
import com.dophuong.submission_service.repository.feign.UserClient;
import com.dophuong.submission_service.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX_QUIZ = "quiz:template:";
    private static final String CACHE_PREFIX_SUBMISSION = "submission:quiz:";

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private SubmissionQuestionRepository submissionQuestionRepository;
    @Autowired
    private SubmissionQuestionOptionRepository submissionQuestionOptionRepository;

    @Autowired
    private CourseClient courseClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private QuizClient quizClient;

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private SubmissionQuestionMapper submissionQuestionMapper;
    @Autowired
    private SubmissionQuestionOptionMapper submissionQuestionOptionMapper;
    @Autowired
    private QuizMapper quizMapper;

    @Override
    public SubmissionResponse startSubmission(Long courseId, Long quizId) {
        // Kiểm tra tồn tại course và quiz
        validateCourseAndQuiz(courseId, quizId);

        // Lấy thông tin user hiện tại
        Long userId = userClient.getUserInfo().getBody().getId();
        String cacheKey = CACHE_PREFIX_SUBMISSION + quizId + ":user:" + userId;

        log.warn("Ai đây: {}", userId);

        // Nếu submission đã có trong Redis -> trả về luôn
        SubmissionResponse cached = (SubmissionResponse) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.warn("Lấy submission từ cache Redis: {}", cacheKey);
            return cached;
        }

        // Kiểm tra thông tin quiz trong cache (nếu có)
        com.example.common_service.dto.response.QuizDetailResponse quizDetailCache = (com.example.common_service.dto.response.QuizDetailResponse) redisTemplate.opsForValue()
                .get(CACHE_PREFIX_QUIZ + quizId);

        QuizDetailResponse quizDetail = quizMapper.toResponse(quizDetailCache);

        if (quizDetail != null) {
            log.warn("Vao day hihi");
            return createSubmissionFromCachedQuiz(courseId, quizId, userId, quizDetail, cacheKey);
        }

        // Nếu không có cache quiz, lấy từ DB/Feign
        QuizResponse quiz = quizClient.getQuiz(courseId, quizId).getBody();
        assert quiz != null;
        if(!quiz.isPublished())
            throw new AppException(ErrorCode.SUBMISSION_NOT_OPEN_YET);
        checkQuizOpenAndCloseTime(quiz.getOpenTime(), quiz.getCloseTime(), courseId, quizId, userId);

        // Nếu có submission đang làm dở -> xử lý
        Submission existing = submissionRepository.getInProgressSubmission(userId, quizId);
        if (existing != null) {
            return handleInProgressSubmission(existing, quiz, courseId, quizId);
        }

        // Kiểm tra số lần làm còn lại
        checkRemainingAttempts(userId, quizId, quizClient.getAttempts(courseId, quizId).getBody());

        // Tạo submission mới
        SubmissionResponse response = submissionRepository.createSubmission(courseId, quizId, userId);
        cacheSubmission(response, cacheKey);
        return response;
    }

    @Override
    public SubmissionResultResponse gradeSubmission(Long quizId, Long submissionId, SubmissionRequest submissionRequest) {
        if(!submissionRepository.exist(submissionId))
            throw new AppException(ErrorCode.SUBMISSION_NOT_FOUND);

        List<SubmissionQuestion> submissionQuestions =
                submissionQuestionRepository.findBySubmissionId(submissionId);

        int correctCount = 0;
        int totalQuestions = submissionQuestions.size();

        for (AnswerRequest answer : submissionRequest.getAnswers()) {
            Long correctId = submissionQuestionOptionRepository.findCorrectInQuestion(answer.getSubmissionQuestionId());
            int check = 0;
            if(correctId == answer.getChosenOptionId()){
                check = 1;
                correctCount += 1;
            }
            submissionQuestionRepository.updateQuestionRs(answer.getSubmissionQuestionId(), check);
            submissionQuestionOptionRepository.updateOptionChosen(answer);
        }

        double score = ((double) correctCount / totalQuestions) * 10;
        int attemptNo = submissionRepository.getAttemptNo(submissionId);
        submissionRepository.updateGrade(submissionId, score);
        Long userId = userClient.getUserInfo().getBody().getId();
        String key = CACHE_PREFIX_SUBMISSION + quizId + ":user:" + userId;
        redisTemplate.delete(key);
        return SubmissionResultResponse.builder()
                .submissionId(submissionId)
                .score(score)
                .attemptNo(attemptNo)
                .build();
    }

    @Override
    public SubmissionReviewResponse reviewSubmission(Long submissionId) {
        // Lấy thông tin submission
        Submission submission = submissionRepository.findById(submissionId);
        if (submission == null) {
            throw new AppException(ErrorCode.SUBMISSION_NOT_FOUND);
        }
        Long userId = userClient.getUserInfo().getBody().getId();

        //Kiểm tra quyền: chỉ xem bài của chính mình
        if (!submission.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        SubmissionReviewResponse reviewResponse = submissionMapper.toReviewResponse(submission);

        // Lấy danh sách câu hỏi trong submission
        List<SubmissionQuestion> submissionQuestions = submissionQuestionRepository.findBySubmissionId(submissionId);
        List<QuestionReviewResponse> listQuestionReviewResponseList = submissionQuestionMapper.toReviewResponseList(submissionQuestions);
        for (QuestionReviewResponse question : listQuestionReviewResponseList) {
            // Lấy danh sách lựa chọn của từng câu
            List<SubmissionQuestionOption> options =
                    submissionQuestionOptionRepository.findBySubQuesId(question.getSubmissionQuestionId());

            // Chuyển sang DTO
            List<OptionReviewResponse> optionResponses = submissionQuestionOptionMapper.toReviewResponseList(options);

            question.setOptions(optionResponses);
        }

        // Trả về kết quả cuối
        reviewResponse.setQuestions(listQuestionReviewResponseList);
        return reviewResponse;
    }

    /** Kiểm tra tồn tại course, quiz và mối quan hệ giữa chúng */
    private void validateCourseAndQuiz(Long courseId, Long quizId) {
        if (Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if (Boolean.FALSE.equals(quizClient.existsQuizId(courseId, quizId).getBody()))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if (Boolean.FALSE.equals(quizClient.existsByCourseIdAndQuizId(courseId, quizId).getBody()))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);
    }

    /** Kiểm tra thời gian mở và đóng của quiz */

    private void checkQuizOpenAndCloseTime(LocalDateTime openTime, LocalDateTime closeTime,
                                           Long courseId, Long quizId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(openTime))
            throw new AppException(ErrorCode.SUBMISSION_NOT_OPEN_YET);
        if (now.isAfter(closeTime)) {
            // Cập nhật submission đang làm thành Kết thúc nếu quiz đã đóng
            Submission existing = submissionRepository.getInProgressSubmission(userId, quizId);
            if (existing != null) {
                submissionRepository.finishSubmission(courseId, quizId, existing.getSubmissionId());
            }
            // Sau khi cập nhật, ném lỗi để báo người dùng không thể làm tiếp
            throw new AppException(ErrorCode.SUBMISSION_CLOSED);
        }
    }

    /** Kiểm tra số lần làm còn lại của user */
    private void checkRemainingAttempts(Long userId, Long quizId, int maxAttempts) {
        int doneAttempts = submissionRepository.countQuizAttempts(userId, quizId);
        if (doneAttempts >= maxAttempts)
            throw new AppException(ErrorCode.SUBMISSION_OUT_OF_ATTEMPTS);
    }

    /** Xử lý khi submission đang làm dở */
    private SubmissionResponse handleInProgressSubmission(Submission existing,
                                                          QuizResponse quiz,
                                                          Long courseId,
                                                          Long quizId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime finishTime = existing.getStartedAt().plusMinutes(quiz.getTimeLimit());

        // Nếu đã hết thời gian thì tự động kết thúc
        if (now.isAfter(finishTime)) {
            submissionRepository.finishSubmission(courseId, quizId, existing.getSubmissionId());
            return submissionRepository.traKetQua(courseId, quizId, existing.getSubmissionId());
        }

        // Nếu vẫn còn thời gian thì trả kết quả đang làm
        return submissionRepository.traKetQua(courseId, quizId, existing.getSubmissionId());
    }

    /** Tạo submission từ quiz cache */
    private SubmissionResponse createSubmissionFromCachedQuiz(Long courseId,
                                                              Long quizId,
                                                              Long userId,
                                                              QuizDetailResponse quizDetail,
                                                              String cacheKey) {
        checkQuizOpenAndCloseTime(quizDetail.getOpenTime(), quizDetail.getCloseTime(), courseId, quizId, userId);
        checkRemainingAttempts(userId, quizId, quizDetail.getAttemptsAllowed());

        int attempt = submissionRepository.countQuizAttempts(userId, quizId) + 1;
        SubmissionResponse response = submissionRepository.daoDe(
                quizId, userId, attempt, courseId, quizDetail.getQuestionResponses()
        );

        cacheSubmission(response, cacheKey);
        return response;
    }

    /** Lưu submission vào cache Redis */
    private void cacheSubmission(SubmissionResponse response, String key) {
        redisTemplate.opsForValue().set(
                key,
                response,
                Duration.ofMinutes(response.getTimeLimit())
        );
        log.info("Đã lưu submission vào Redis: {}", key);
    }

}
