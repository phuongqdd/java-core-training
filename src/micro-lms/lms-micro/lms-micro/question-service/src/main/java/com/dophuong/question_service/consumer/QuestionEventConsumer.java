//package com.dophuong.question_service.consumer;
//
//import com.dophuong.question_service.entity.QuestionEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.List;
//
//@Service
//@Slf4j
//public class QuestionEventConsumer {
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    private static final String CACHE_PREFIX = "courseQuestions:";
//
//    @KafkaListener(topics = "question", groupId = "questionGroup")
//    public void consume(QuestionEvent event) {
//        String key = CACHE_PREFIX + event.getCourseId() + ":" + event.getDifficulty();
//        List<Long> cached = (List<Long>) redisTemplate.opsForValue().get(key);
//
//        switch (event.getAction()) {
//            case "ADD":
//                if (cached != null) {
//                    cached.add(event.getQuestionId());
//                    updateCache(event.getCourseId(), event.getDifficulty(), cached);
//                } else {
//                    updateCache(event.getCourseId(), event.getDifficulty(), List.of(event.getQuestionId()));
//                }
//                log.info("Đã thêm trong {} với id: {}", key, event.getQuestionId());
//                break;
//
//            case "DELETE":
//                removeCacheQuestion(event.getCourseId(), event.getQuestionId(), event.getDifficulty());
//                log.info("Đã xóa trong {} với id: {}", key, event.getQuestionId());
//                break;
//
//            case "UPDATE":
//                // Xóa khỏi list cũ và thêm vào list mới nếu đổi độ khó
//                if (!event.getDifficulty().equals(event.getOldDifficulty())) {
//                    // Xóa câu hỏi khỏi danh sách của độ khó cũ
//                    removeCacheQuestion(event.getCourseId(), event.getQuestionId(), event.getOldDifficulty());
//                    log.info("Đã xóa trong {}:{} với id {}", event.getCourseId(), event.getOldDifficulty(), event.getQuestionId());
//
//                    // Thêm vào danh sách của độ khó mới
//                    String newKey = CACHE_PREFIX + event.getCourseId() + ":" + event.getDifficulty();
//                    List<Long> newCache = (List<Long>) redisTemplate.opsForValue().get(newKey);
//                    if (newCache != null) {
//                        if (!newCache.contains(event.getQuestionId())) {
//                            newCache.add(event.getQuestionId());
//                        }
//                    } else {
//                        newCache = List.of(event.getQuestionId());
//                    }
//                    updateCache(event.getCourseId(), event.getDifficulty(), newCache);
//                    newCache = (List<Long>) redisTemplate.opsForValue().get(newKey);
//                    log.info("Đã cập nhật trong {} với id: {}", newKey, event.getQuestionId());
//                }
//                break;
//        }
//
//        System.out.println(" Updated Redis for event: " + event);
//    }
//
//    private void updateCache(Long courseId, String level, List<Long> questionIds) {
//        String key = CACHE_PREFIX + courseId + ":" + level;
//        log.warn("Du lieu: {}", questionIds);
//        redisTemplate.opsForValue().set(key, questionIds, Duration.ofHours(24));
//    }
//
//    private void removeCacheQuestion(Long courseId, Long questionId, String level){
//        String key = CACHE_PREFIX + courseId + ":" + level;
//        List<Long> cached = (List<Long>) redisTemplate.opsForValue().get(key);
//        if (cached != null) {
//            cached.remove(questionId);
//            updateCache(courseId, level, cached);
//        }
//    }
//
//}
