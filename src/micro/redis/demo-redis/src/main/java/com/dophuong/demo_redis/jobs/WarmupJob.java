package com.dophuong.demo_redis.jobs;

import com.dophuong.demo_redis.entity.Product;
import com.dophuong.demo_redis.service.ProductService;
import com.dophuong.demo_redis.service.RelatedProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class WarmupJob {

    private final ProductService productService;
    private final RelatedProductService relatedService;

    @Value("${app.jobs.warmup-batch-size:100}")
    private int batchSize;

    private final AtomicReference<Instant> lastRun = new AtomicReference<>(Instant.EPOCH);

    @Scheduled(fixedDelayString = "${app.jobs.warmup-fixed-delay:PT1M}")
    public void run() {
        Instant since = lastRun.getAndSet(Instant.now());
        List<Product> updated = productService.findUpdateSince(since, batchSize);

        for (Product p : updated) {
            productService.warmUp(p.getId());   // product:{id}
            relatedService.recomputeAndCache(p.getId()); // related:{id}
        }
        if (!updated.isEmpty()) {
            log.info("Warmup cached products={}, since={}", updated.size(), since);
        }
    }
}
