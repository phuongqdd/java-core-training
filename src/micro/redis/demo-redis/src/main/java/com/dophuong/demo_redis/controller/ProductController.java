package com.dophuong.demo_redis.controller;


import com.dophuong.demo_redis.dto.request.ProductCreateRequest;
import com.dophuong.demo_redis.dto.request.ProductUpdateRequest;
import com.dophuong.demo_redis.dto.response.ProductResponse;
import com.dophuong.demo_redis.dto.response.RelatedResponse;
import com.dophuong.demo_redis.entity.Product;
import com.dophuong.demo_redis.service.ProductService;
import com.dophuong.demo_redis.service.RelatedProductService;
import com.dophuong.demo_redis.service.RelatedQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RelatedProductService relatedService;
    private final RelatedQueueService queue;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest req){
        return ResponseEntity.ok(productService.createProduct(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable long id,
                                                  @Valid @RequestBody ProductUpdateRequest req){
        return ResponseEntity.ok(productService.updateProduct(id, req));
    }

    // lấy related (cache-first)
    @GetMapping("/{id}/related")
    public ResponseEntity<RelatedResponse> related(@PathVariable long id){
        return ResponseEntity.ok(relatedService.getRelated(id));
    }

    // test: đẩy tính related vào queue bằng tay
    @PostMapping("/{id}/enqueue-related")
    public ResponseEntity<String> enqueue(@PathVariable long id){
        queue.enqueue(id);
        return ResponseEntity.ok("queued " + id);
    }

}
