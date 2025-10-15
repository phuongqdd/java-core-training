package com.dophuong.demo_redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.keyvalue.core.event.KeyValueEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatedQueueService {

    public static final String RELATED_QUEUE_CACHE = "related:queue";

    private final StringRedisTemplate srt;

    public void enqueue(long productId){
        srt.opsForList().leftPush(RELATED_QUEUE_CACHE, Long.toString(productId));
    }
    public Long tryDequeue(){
        String v = srt.opsForList().rightPop(RELATED_QUEUE_CACHE);
        return v == null ? null : Long.valueOf(v);
    }
}
