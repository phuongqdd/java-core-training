package com.dophuong.demo_redis.service;

import com.dophuong.demo_redis.dto.response.RelatedResponse;

import java.util.List;

public interface RelatedProductService {

    RelatedResponse getRelated(long productId);

    RelatedResponse recomputeAndCache(long productId);

    List<Long> recompute(Long productId);

}
