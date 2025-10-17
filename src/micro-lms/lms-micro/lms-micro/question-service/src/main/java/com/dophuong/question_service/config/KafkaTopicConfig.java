package com.dophuong.question_service.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic questionTopic(){
        return TopicBuilder
                .name("question")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
