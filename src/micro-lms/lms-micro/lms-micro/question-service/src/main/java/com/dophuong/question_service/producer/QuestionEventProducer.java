//package com.dophuong.question_service.producer;
//
//import com.dophuong.question_service.entity.QuestionEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class QuestionEventProducer {
//    private final KafkaTemplate<String, QuestionEvent> kafkaTemplate;
//
//    public void sendQuestionEvent(QuestionEvent event) {
//        kafkaTemplate.send("question", event);
//        log.warn("✅Sent Kafka event: {}", event);
//    }
//}
