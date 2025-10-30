package com.dophuong.quiz_service.service;

import com.dophuong.quiz_service.dto.response.QuizDetailResponse;
import com.dophuong.quiz_service.entity.Quiz;
import com.dophuong.quiz_service.mapper.QuizMapper;
import com.dophuong.quiz_service.repository.QuizRepository;
import com.dophuong.quiz_service.repository.feign.QuestionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class QuizCacheService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "quiz:template:";

    @Scheduled(fixedRate = 300_000) // 5 phút
    public void preloadQuizs() {
        List<Quiz> quizs = getAllQuizIds();
        LocalDateTime now = LocalDateTime.now();


        for (Quiz quiz : quizs) {
            // Kiểm tra quiz có còn hợp lệ (đã mở và chưa đóng)
            if (!quiz.isPublished()) {
                redisTemplate.delete(CACHE_PREFIX + quiz.getId());
                continue;
            }

            LocalDateTime openTime = quiz.getOpenTime();
            LocalDateTime closeTime = quiz.getCloseTime();

            if (now.isAfter(openTime) && now.isBefore(closeTime)) {
                // Quiz hợp lệ → lưu vào Redis với TTL
                String key = CACHE_PREFIX + quiz.getId();
                QuizDetailResponse quizDetailResponse = quizService.getQuizDetail(quiz.getCourseId(), quiz.getId());
                com.example.common_service.dto.response.QuizDetailResponse response = quizMapper.toResponseDetail(quizDetailResponse);
                // Tính thời gian sống còn lại
                Duration ttl = Duration.between(now, closeTime);
                if (!ttl.isNegative() && !ttl.isZero()) {
                    redisTemplate.opsForValue().set(key, response, ttl);
                } else {
                    // Nếu hết hạn → xóa cache nếu có
                    redisTemplate.delete(key);
                }

            } else {
                // Quiz chưa mở hoặc đã hết hạn → xóa cache nếu có
                redisTemplate.delete(CACHE_PREFIX + quiz.getId());
            }
        }
    }


    private List<Quiz> getAllQuizIds() {
        return quizRepository.findAllQuizIds();
    }
}
