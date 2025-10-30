package com.dophuong.submission_service.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class RedisConfig {

    // Redis host + port lấy từ application.yml
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    /**
     * Tạo JedisConnectionFactory với standalone config + connection pool
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        //Cấu hình standalone (host, port, password)
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisHost);
        standaloneConfig.setPort(redisPort);
        if (!redisPassword.isEmpty()) {
            standaloneConfig.setPassword(redisPassword);
        }

        //Cấu hình connection pool
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);                   // Số kết nối tối đa trong pool
        poolConfig.setMaxIdle(10);                    // Số kết nối nhàn rỗi tối đa
        poolConfig.setMinIdle(1);                     // Số kết nối nhàn rỗi tối thiểu
        poolConfig.setTestWhileIdle(true);            // Kiểm tra kết nối nhàn rỗi trước khi cấp
        poolConfig.setMinEvictableIdleTime(Duration.ofMinutes(1)); // Loại bỏ kết nối idle > 1 phút
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30)); // Kiểm tra kết nối idle mỗi 30s

        // Cấu hình Jedis client với pool và timeout
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofSeconds(10)) // timeout khi kết nối Redis
                .readTimeout(Duration.ofSeconds(10))    // timeout khi đọc dữ liệu
                .usePooling()
                .poolConfig(poolConfig)
                .build();

        return new JedisConnectionFactory(standaloneConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Key dùng String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value dùng JSON, hỗ trợ LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // hỗ trợ Java 8 Date/Time
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // serialize thành ISO-8601
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        // Gắn connection factory
        template.setConnectionFactory(redisConnectionFactory());

        template.afterPropertiesSet();
        return template;
    }


}