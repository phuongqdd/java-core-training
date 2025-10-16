package com.example.kafka_demo_1;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "demoone", groupId = "groupId")
    void listener1(String data){
        System.out.println("Listener received 1: " + data + " 🤘");
    }

    @KafkaListener(topics = "demoone", groupId = "groupId")
    void listener2(String data){
        System.out.println("Listener received 2: " + data + " 🤘");
    }

}
