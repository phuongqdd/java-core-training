package com.example.kafka_demo_1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaDemo1Application {

	public static void main(String[] args) {
		SpringApplication.run(KafkaDemo1Application.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
//		return args -> {
//			for (int i = 0; i < 100; i++) {
//				kafkaTemplate.send("demoone", "Hello kafka :) " + i);
//			}
//		};
//	}

}
