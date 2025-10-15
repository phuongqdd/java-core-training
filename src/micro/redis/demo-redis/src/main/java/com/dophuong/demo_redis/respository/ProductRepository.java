package com.dophuong.demo_redis.respository;

import com.dophuong.demo_redis.entity.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUpdatedAtAfter(Instant since, PageRequest pageable);
    List<Product> findTop20ByCategoryIdAndIdNotOrderByIdDesc(Long categoryId, Long excludeId);
}
