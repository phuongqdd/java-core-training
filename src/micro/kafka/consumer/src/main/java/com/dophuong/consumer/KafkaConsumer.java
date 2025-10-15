package com.dophuong.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

//    @KafkaListener(topics = "new-topic", groupId = "my-new-group")
//    public void listen1(String message){
//        System.out.println("Received Message 1: " + message);
//    }

    @KafkaListener(topics = "new-topic", groupId = "my-new-group-rider")
    public void listen2(RiderLocation riderLocation){
        System.out.println("Received Message 2: " + riderLocation);
    }

}
