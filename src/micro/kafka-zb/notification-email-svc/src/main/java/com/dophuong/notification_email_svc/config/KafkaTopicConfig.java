package com.dophuong.notification_email_svc.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic ordereventsTopic(){
        return TopicBuilder
                .name("order.events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
