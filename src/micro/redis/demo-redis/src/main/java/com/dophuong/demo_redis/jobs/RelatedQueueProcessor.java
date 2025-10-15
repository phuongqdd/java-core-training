package com.dophuong.demo_redis.jobs;

import com.dophuong.demo_redis.service.RelatedProductService;
import com.dophuong.demo_redis.service.RelatedQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RelatedQueueProcessor {

//    private final RelatedQueueService relatedQueueService;
//
//    private final RelatedProductService relatedProductService;
//
//    @Value("${app.jobs.queue-batch-size:100}")
//    private int batchSize;
//
//    @Scheduled(fixedDelayString = "${app.jobs.queue-process-fixed-delay:PT2S}")
//    public void process(){
//        int processed = 0;
//        for(processed = 0 ; processed < batchSize; processed++){
//            Long pid = relatedQueueService.tryDequeue();;
//            if(pid == null)
//                break;
//            relatedProductService.recomputeAndCache(pid);
//        }
//
//        if(processed > 0)
//            log.warn("Processed {} items from related queue", processed);
//    }

}
