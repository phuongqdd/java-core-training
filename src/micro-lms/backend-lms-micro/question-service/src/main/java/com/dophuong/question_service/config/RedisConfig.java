package com.dophuong.question_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class RedisConfig {

//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    /**
//     * Tạo connection factory kết nối Redis với pooling
//     */
//    @Bean
//    public JedisConnectionFactory redisConnectionFactory() {
//        // 1. Cấu hình standalone (host, port, password)
//        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
//        standaloneConfig.setHostName(redisHost);
//        standaloneConfig.setPort(redisPort);
////        standaloneConfig.setPassword("12345678");
//
//        // 2. Cấu hình connection pool
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(50);                   // Số kết nối tối đa trong pool
//        poolConfig.setMaxIdle(10);                    // Số kết nối nhàn rỗi tối đa
//        poolConfig.setMinIdle(1);                     // Số kết nối nhàn rỗi tối thiểu
//        poolConfig.setTestWhileIdle(true);            // Kiểm tra kết nối nhàn rỗi trước khi cấp
//        poolConfig.setMinEvictableIdleTime(Duration.ofMinutes(1)); // Loại bỏ kết nối idle > 1 phút
//        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30)); // Kiểm tra kết nối idle mỗi 30s
//
//        // 3. Cấu hình Jedis client với pool và timeout
//        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
//                .connectTimeout(Duration.ofSeconds(10)) // timeout khi kết nối
//                .readTimeout(Duration.ofSeconds(10))    // timeout khi đọc dữ liệu
//                .usePooling()
//                .poolConfig(poolConfig)
//                .build();
//
//        return new JedisConnectionFactory(standaloneConfig, clientConfig);
//    }
//
//    /**
//     * RedisTemplate để thao tác với Redis
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setConnectionFactory(redisConnectionFactory());
//        return template;
//    }
//
//    /** Queue: string thuần (an toàn, tránh type-id) */
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
//        return new StringRedisTemplate(cf);
//    }
}