package com.dophuong.producer;

//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class KafkaProducer {
//
//    private final KafkaTemplate<String, RiderLocation> kafkaTemplate;
//
//    @PostMapping("/send")
//    public String sendMessage(@RequestParam String message){
//        RiderLocation riderLocation = RiderLocation.builder()
//                .riderId("id1234")
//                .latitude(45.67)
//                .longitude(47.89)
//                .build();
//        kafkaTemplate.send("new-topic", riderLocation);
//        return "Message sent: " + riderLocation.getRiderId();
//    }
//
//}
