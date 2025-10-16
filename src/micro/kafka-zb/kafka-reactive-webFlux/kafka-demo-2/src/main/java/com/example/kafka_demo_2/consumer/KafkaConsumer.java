package com.example.kafka_demo_2.consumer;

import com.example.kafka_demo_2.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

//    @KafkaListener(topics = "demotwo", groupId = "myGroup")
//    public void consumeMsg(String msg){
//        log.info("Consuming the message from demotwo Topic:: {}", msg);
//    }

    @KafkaListener(topics = "demotwo", groupId = "myGroup")
    public void consumeJsonMsg(Student student){
        log.info("Consuming the message json from demotwo Topic1:: {}", student);
    }

    @KafkaListener(topics = "demotwo", groupId = "myGroup")
    public void consumeJsonMsg1(Student student){
        log.info("Consuming the message json from demotwo Topic2:: {}", student);
    }

}
