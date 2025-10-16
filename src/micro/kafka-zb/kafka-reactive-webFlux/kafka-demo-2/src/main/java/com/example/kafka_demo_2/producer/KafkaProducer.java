package com.example.kafka_demo_2.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg){
        log.info("Sending message to demotwo Topic:: {}", msg);
        kafkaTemplate.send("demotwo", msg);
    }

}
