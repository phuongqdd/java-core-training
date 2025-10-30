package com.dophuong.question_service.service;

import com.dophuong.question_service.repository.QuestionRepository;
import com.dophuong.question_service.repository.feign.CourseClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class QuestionCacheService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "warmupExecutor")
    private Executor warmupExecutor;

    // Prefix key chuẩn cho Redis
    private static final String CACHE_PREFIX = "courseQuestions:";

    /**
     * Lấy question IDs theo course + level từ Redis
     * Nếu chưa có cache thì load từ DB và set vào Redis với TTL 24h
     */
    public List<Long> getQuestionsByLevel(Long courseId, String level) {
        String key = CACHE_PREFIX + courseId + ":" + level;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return (List<Long>) cached;
        }

        // Nếu chưa có cache, load từ DB
        List<Long> questionIds = questionRepository.getQuestionsByLevel(courseId, level);

        // Set vào Redis với TTL 24h
        redisTemplate.opsForValue().set(key, questionIds, Duration.ofHours(24));
        return questionIds;
    }

    /**
     * Preload cache cho tất cả course + level
     * Chạy mỗi 5 phút để đảm bảo dữ liệu mới được cache
     */
    @Scheduled(fixedRate = 300_000) // 5 phút
    public void preloadQuestions() {
        List<Long> courseIds = getAllCourseIds();
        List<String> levels = List.of("EASY", "MEDIUM", "HARD", "VERY_HARD");
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(Long courseId : courseIds){
            for (String level : levels){
                CompletableFuture<Void> f = CompletableFuture.runAsync(() -> {
                    try {
                        getQuestionsByLevel(courseId, level);
                    }catch (Exception e){

                    }

                }, warmupExecutor);
                futures.add(f);
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.warn("Chay xong luc {}", LocalDateTime.now());
    }


    /**
     * Lấy tất cả courseId từ CourseClient
     */
    private List<Long> getAllCourseIds() {
        return courseClient.getCourseId().getBody();
    }
}

