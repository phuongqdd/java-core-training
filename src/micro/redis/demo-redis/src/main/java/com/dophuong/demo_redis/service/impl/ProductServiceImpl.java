package com.dophuong.demo_redis.service.impl;

import com.dophuong.demo_redis.dto.request.ProductCreateRequest;
import com.dophuong.demo_redis.dto.request.ProductUpdateRequest;
import com.dophuong.demo_redis.dto.response.ProductResponse;
import com.dophuong.demo_redis.entity.Category;
import com.dophuong.demo_redis.entity.Product;
import com.dophuong.demo_redis.mapper.ProductMapper;
import com.dophuong.demo_redis.respository.CategoryRepository;
import com.dophuong.demo_redis.respository.ProductRepository;
import com.dophuong.demo_redis.service.ProductService;
import com.dophuong.demo_redis.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCT_CACHE = "product:";
    public static final String RELATED_CACHE = "related:";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    private final RedisCacheService redisCacheService;

    @Value("${app.cache.product-ttl-seconds:86400}")
    private long productTtl;

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found!!"));
        Product product = productMapper.toEntity(request);

        product = productRepository.save(product);

        redisCacheService.delete(PRODUCT_CACHE + product.getId());

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProduct(Long productId) {
        String key = PRODUCT_CACHE + productId;
        ProductResponse cachedProduct = redisCacheService.getValue(key, ProductResponse.class);
        log.warn("Giá trị đọc từ Redis: {}", cachedProduct);
        if (cachedProduct != null) {
            log.warn("Đã lấy từ casche: {}", cachedProduct);
            return cachedProduct; // Nếu có cache thì trả luôn
        }

        log.warn("Đã lấy từ db: {}", 89);
        // Nếu không có cache, lấy từ DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id = " + productId));

        ProductResponse response = productMapper.toResponse(product);

        // Lưu vào Redis cache
        redisCacheService.setValue(key, response, productTtl, TimeUnit.SECONDS);
        log.warn("Đã lưu cache Redis với key {}", key);
        return response;
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest updateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));

        Category category = categoryRepository.findById(updateRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found!!"));

        product.setName(updateRequest.getName());
        product.setPrice(updateRequest.getPrice());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        ProductResponse response = productMapper.toResponse(updatedProduct);

        redisCacheService.delete(PRODUCT_CACHE + response.getId());
        redisCacheService.delete(RELATED_CACHE + response.getId());

        return response;
    }

    @Override
    public void deleteProduct(Long productId) {
        // Xóa DB
        productRepository.deleteById(productId);

        // Xóa cache
        redisCacheService.delete(PRODUCT_CACHE + productId);
        redisCacheService.delete(RELATED_CACHE + productId);
    }

    @Override
    public List<Product> findUpdateSince(Instant since, int limit) {
        return productRepository.findByUpdatedAtAfter(since, PageRequest.of(0, limit));
    }

    @Override
    public void warmUp(long id) {
        if(!redisCacheService.exists(PRODUCT_CACHE + id))
            getProduct(id);
    }
}
