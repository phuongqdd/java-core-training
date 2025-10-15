package com.dophuong.demo_redis.service;

import com.dophuong.demo_redis.dto.request.ProductCreateRequest;
import com.dophuong.demo_redis.dto.request.ProductUpdateRequest;
import com.dophuong.demo_redis.dto.response.ProductResponse;
import com.dophuong.demo_redis.entity.Product;

import java.time.Instant;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse getProduct(Long productId);

    ProductResponse updateProduct(Long productId, ProductUpdateRequest updateRequest);

    void deleteProduct(Long productId);

    List<Product> findUpdateSince(Instant since, int limit);

    void warmUp(long id);

}
