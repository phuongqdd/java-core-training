package com.example.kafka_demo_1.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Tạo ra Map chứa các cấu hình cho Kafka Consumer
    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();

        // Địa chỉ của Kafka broker để consumer kết nối
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Dùng StringDeserializer để giải mã (deserialize) key từ dạng byte → String
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Dùng StringDeserializer để giải mã value từ dạng byte → String
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // ID của consumer group (mỗi consumer group chia sẻ load cùng topic)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");

        // Nếu chưa có offset nào, thì đọc từ đầu topic (earliest) thay vì cuối (latest)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    // Khai báo bean ConsumerFactory để Spring tạo consumer khi cần
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        // DefaultKafkaConsumerFactory sẽ dùng map config bên trên để tạo Consumer instance
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    // Tạo container factory dùng cho @KafkaListener
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        // Container này giúp quản lý nhiều consumer chạy song song (multi-thread)
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Gán consumer factory ở trên để container biết cách tạo consumer
        factory.setConsumerFactory(consumerFactory);

        // Trả về factory này để Spring Boot tự động cấu hình @KafkaListener
        return factory;
    }
}
