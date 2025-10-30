package com.dophuong.quiz_service.service.impl;

import com.dophuong.quiz_service.dto.request.AddQuestionToQuizRequest;
import com.dophuong.quiz_service.dto.request.QuizCreateRequest;
import com.dophuong.quiz_service.dto.request.QuizUpdateRequest;
import com.dophuong.quiz_service.dto.response.*;
import com.dophuong.quiz_service.entity.Quiz;
import com.dophuong.quiz_service.entity.QuizHistory;
import com.dophuong.quiz_service.enums.ActionType;
import com.dophuong.quiz_service.enums.Difficulty;
import com.dophuong.quiz_service.enums.ErrorCode;
import com.dophuong.quiz_service.exception.AppException;
import com.dophuong.quiz_service.mapper.QuizMapper;
import com.dophuong.quiz_service.repository.QuizHistoryRepository;
import com.dophuong.quiz_service.repository.QuizQuestionRepository;
import com.dophuong.quiz_service.repository.QuizRepository;
import com.dophuong.quiz_service.repository.feign.CourseClient;
import com.dophuong.quiz_service.repository.feign.QuestionClient;
import com.dophuong.quiz_service.repository.feign.UserClient;
import com.dophuong.quiz_service.service.QuizService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX_QUIZ = "quiz:template:";

    @Autowired
    private QuizHistoryRepository quizHistoryRepository;

    @Autowired
    private CourseClient courseClient;
    @Autowired
    private QuestionClient questionClient;
    @Autowired
    private UserClient userClient;

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Override
    public Boolean exists(Long quizId) {
        return quizRepository.existsById(quizId);
    }

    @Override
    public Boolean existsByCourseIdAndQuizId(Long courseId, Long quizId) {
        return quizRepository.existsByCourseIdAndQuizId(courseId, quizId);
    }

    @Override
    public QuizResponse createQuiz(Long courseId, QuizCreateRequest request) {

        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        Quiz quiz = quizMapper.toEntity(request);
        quiz.setCourseId(courseId);

        if (request.getOpenTime() != null && request.getCloseTime() != null) {
            long availableMinutes = java.time.Duration.between(request.getOpenTime(), request.getCloseTime()).toMinutes();
            if (request.getTimeLimit() > availableMinutes) {
                throw new AppException(ErrorCode.TIME_LIMIT_EXCEEDS_CLOSE_TIME);
            }
        }

        List<Integer> totalLevel = validateToTalQuizRequest(quiz, courseId);

        quiz = quizRepository.createQuiz(quiz);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse userResponse = userClient.getByUsername(userName).getBody();

        quizRepository.createQuizRandomQuestion(quiz.getId(), totalLevel, courseId);
        QuizHistory quizHistory = QuizHistory.builder()
                .quiz(quiz)
                .userId(userResponse.getId())
                .actionType(ActionType.CREATED)
                .time(LocalDateTime.now())
                .build();
        quizHistoryRepository.save(quizHistory);

        String key = CACHE_PREFIX_QUIZ + quiz.getId();
        LocalDateTime openTime = quiz.getOpenTime();
        LocalDateTime closeTime = quiz.getCloseTime();
        LocalDateTime now = LocalDateTime.now();

        QuizDetailResponse cached = (QuizDetailResponse) redisTemplate.opsForValue().get(key);
        if (cached == null) {

            if (quiz.isPublished() && now.isAfter(openTime) && now.isBefore(closeTime)) {
                // Quiz hợp lệ → lưu vào Redis với TTL
                QuizDetailResponse quizDetailResponse = getQuizDetail(quiz.getCourseId(), quiz.getId());

                // Tính thời gian sống còn lại
                Duration ttl = Duration.between(now, closeTime);
                if (!ttl.isNegative() && !ttl.isZero()) {
                    redisTemplate.opsForValue().set(key, quizDetailResponse, ttl);
                }
            }
        }

        return quizMapper.toResponse(quiz);
    }

    @Override
    public QuizDetailResponse getQuizDetail(Long courseId, Long quizId) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        String key = CACHE_PREFIX_QUIZ + quizId;
        com.example.common_service.dto.response.QuizDetailResponse cached = (com.example.common_service.dto.response.QuizDetailResponse) redisTemplate.opsForValue().get(key);
        if(cached == null){
            return traLai(courseId, quizId);
        }else {
            return quizMapper.toResponseDetailQS(cached);
        }
    }

    @Override
    public void deleteQuestionFromQuiz(Long courseId, Long quizId, Long questionId) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(Boolean.FALSE.equals(questionClient.exists(courseId, questionId).getBody()))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsQuestionInQuiz(quizId, questionId))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse userResponse = userClient.getByUsername(userName).getBody();

        quizRepository.deleteQuestionInQuiz(courseId, quizId, questionId, userResponse.getId());
    }

    @Override
    public void addQuestionToQuiz(Long courseId, Long quizId, AddQuestionToQuizRequest request) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(Boolean.FALSE.equals(questionClient.exists(courseId, request.getQuestionId()).getBody()))
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        if(quizRepository.existsQuestionInQuiz(quizId, request.getQuestionId()))
            throw new AppException(ErrorCode.QUESTION_EXISTED);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse userResponse = userClient.getByUsername(userName).getBody();

        quizRepository.addQuestionToQuiz(quizId, userResponse.getId(), request.getQuestionId(), courseId);
    }

    @Override
    public void updateQuiz(Long courseId, Long quizId, QuizUpdateRequest request) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        UserResponse user = userClient.getUserInfo().getBody();
        Quiz quiz = quizMapper.toEntityUpdate(request);
        quizRepository.updateQuiz(quizId, user.getId(),quiz);
    }

    @Override
    public void deleteQuiz(Long courseId, Long quizId) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        if(!quizRepository.existsById(quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_FOUND);

        if(!quizRepository.existsByCourseIdAndQuizId(courseId, quizId))
            throw new AppException(ErrorCode.QUIZ_NOT_IN_COURSE);

        quizRepository.deleteQuizById(quizId);
    }

    @Override
    public List<QuizSummaryResponse> getQuizzes(Long courseId) {
        if(Boolean.FALSE.equals(courseClient.exists(courseId).getBody()))
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse userResponse = userClient.getByUsername(userName).getBody();
        boolean isInstructor = Boolean.TRUE.equals(courseClient.hasRole(courseId, userName).getBody());
        boolean isAdmin = false;

        for (String ur : userResponse.getRoles()) {
            if ("ADMIN".equals(ur)) {
                isAdmin = true;
            }
        }
        List<Quiz> quizList = new ArrayList<>();
        if(isAdmin || isInstructor){
            log.warn("Con IN trong cou: {}", isInstructor);
            log.warn("Con admin: {}", userResponse.getRoles());
            quizList = quizRepository.findAllByCourseId(courseId);
        } else {
            log.warn("Con IN trong cou: {}", isInstructor);
            log.warn("Con admin: {}", userResponse.getRoles());
            quizList = quizRepository.findAllByCourseIdForStudent(courseId);
        }

        List<QuizSummaryResponse> quizSummaryResponses = quizMapper.toQuizSummaryResponses(quizList);

        return quizSummaryResponses;
    }

    @Override
    public Integer getAttempts(Long quizId) {
        return quizRepository.findAttemptsById(quizId);
    }

    @Override
    public List<QuestionResponse> getAllQuestionDetailByQuizId(Long courseId, Long quizId) {
        return quizRepository.findAllQuestionDetailByQuizId(courseId, quizId);
    }

    @Override
    public QuizResponse getQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId);
        return quizMapper.toResponse(quiz);
    }

    private List<Integer> validateToTalQuizRequest(Quiz quiz, Long courseId) {
        if(questionClient.getToTal(courseId).getBody() < quiz.getTotal())
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

        Map<Difficulty, Integer> questionsByDifficulty = questionClient.getDiff(courseId).getBody();

        List<Integer> lv = calculateQuestionDistribution(quiz.getTotal(), quiz.getPctRl(), quiz.getPctUn(), quiz.getPctAp(), quiz.getPctAn());

        if(lv.get(0) > questionsByDifficulty.get(Difficulty.EASY) ||
                lv.get(1) > questionsByDifficulty.get(Difficulty.MEDIUM) ||
                lv.get(2) > questionsByDifficulty.get(Difficulty.HARD) ||
                lv.get(3) > questionsByDifficulty.get(Difficulty.VERY_HARD))
            throw new AppException(ErrorCode.INSUFFICIENT_QUESTION_BANK);

        if(quiz.getPctAn() > 10)
            throw new AppException(ErrorCode.PERCENTAGE_AN_TOO_HIGH);

        // Trả về kết quả
        return lv;
    }

    private List<Integer> calculateQuestionDistribution(int total, int pctRl, int pctUn, int pctAp, int pctAn) {
        float[] pcts = {pctRl, pctUn, pctAp, pctAn};
        int[] counts = new int[4];
        float[] remainders = new float[4];
        int sum = 0;

        // Tính số câu bằng floor và phần dư
        for (int i = 0; i < 4; i++) {
            float exact = total * pcts[i] / 100f;
            counts[i] = (int) Math.floor(exact);
            remainders[i] = exact - counts[i];
            sum += counts[i];
        }

        // Cộng thêm phần thiếu (diff) vào các nhóm có phần dư lớn nhất
        int diff = total - sum;
        while (diff > 0) {
            int maxIdx = 0;
            for (int j = 1; j < 4; j++) {
                if (remainders[j] > remainders[maxIdx]) {
                    maxIdx = j;
                }
            }
            counts[maxIdx]++;
            remainders[maxIdx] = -1; // đánh dấu đã cộng
            diff--;
        }

        // Trả về danh sách theo thứ tự: [EASY, MEDIUM, HARD, VERY_HARD]
        return List.of(counts[0], counts[1], counts[2], counts[3]);
    }

    public QuizDetailResponse traLai(Long courseId, Long quizId){
        Quiz quiz = quizRepository.findById(quizId);

        List<QuestionResponse> questionResponses = quizQuestionRepository.findTestDetailsByQuizId(courseId, quizId);

        QuizDetailResponse quizDetailResponse = quizMapper.toQuizDetailResponse(quiz);

        quizDetailResponse.setQuestionResponses(questionResponses);

        return quizDetailResponse;
    }
}
