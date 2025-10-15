package com.dophuong.demo_redis.service.impl;

import com.dophuong.demo_redis.dto.response.RelatedResponse;
import com.dophuong.demo_redis.entity.Product;
import com.dophuong.demo_redis.respository.ProductRepository;
import com.dophuong.demo_redis.service.RedisCacheService;
import com.dophuong.demo_redis.service.RelatedProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelatedProductServiceImpl implements RelatedProductService {

    public static final String PRODUCT_CACHE = "product:";
    public static final String RELATED_CACHE = "related:";

    private final ProductRepository productRepository;

    private final RedisCacheService redisCacheService;

    @Value("${app.cache.related-ttl-seconds:86400}")
    private long relatedTtl;


    @Override
    public RelatedResponse getRelated(long productId) {
        String key = RELATED_CACHE + productId;

        @SuppressWarnings("unchecked")
        List<Long> cached = (List<Long>) redisCacheService.getValue(key, List.class);

        if(cached != null){
            log.warn("Đã vào cackhe: {}", key);
            log.warn("Dữ liệu {}:", cached);
            log.warn("Dữ liệu type {}:", cached.get(0).getClass().getName());
            return RelatedResponse.builder()
                    .productId(productId)
                    .relatedIds(cached)
                    .build();
        }

        List<Long> computed = recompute(productId);
        redisCacheService.setValue(key, computed, relatedTtl, TimeUnit.SECONDS);
        return new RelatedResponse(productId, computed);
    }

    @Override
    public RelatedResponse recomputeAndCache(long productId) {
        List<Long> data = recompute(productId);
        redisCacheService.setValue(RELATED_CACHE + productId, data, relatedTtl, TimeUnit.SECONDS);
        return new RelatedResponse(productId, data);
    }

    @Override
    public List<Long> recompute(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        return productRepository.findTop20ByCategoryIdAndIdNotOrderByIdDesc(categoryId, productId)
                .stream().map(Product::getId).toList();
    }
}
