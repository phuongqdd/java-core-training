package com.dophuong.demo_redis;

import com.dophuong.demo_redis.dto.response.ProductResponse;
import com.dophuong.demo_redis.entity.Product;
import com.dophuong.demo_redis.mapper.ProductMapper;
import com.dophuong.demo_redis.respository.ProductRepository;
import com.dophuong.demo_redis.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class DemoRedisApplicationTests {

	@Container
	@ServiceConnection
	static GenericContainer<?> redis =
			new GenericContainer<>(DockerImageName.parse("redis:7.4.2"))
					.withExposedPorts(6379);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CacheManager cacheManager;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		productRepository.deleteAll(); // làm sạch DB trước mỗi test
		Cache cache = cacheManager.getCache(ProductServiceImpl.PRODUCT_CACHE);
		if (cache != null) cache.clear(); // làm sạch cache
	}

	@Test
	void testCreateProductAndCacheIt() throws Exception {
		ProductRequest request = ProductRequest.builder()
				.name("Laptop")
				.price(BigDecimal.valueOf(1200L))
				.build();

		MvcResult result = mockMvc.perform(post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andReturn();

		ProductResponse created = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);
		Long productId = created.getId();

		// DB check
		Assertions.assertTrue(productRepository.findById(productId).isPresent());

		// Cache check
		Cache cache = cacheManager.getCache(ProductServiceImpl.PRODUCT_CACHE);
		assertNotNull(cache);

		ProductResponse cached = cache.get(productId, ProductResponse.class); // chú ý: dùng overload get(key, class)
		assertNotNull(cached);
		Assertions.assertEquals("Laptop", cached.getName());
	}

	@Test
	void testGetProductAndVerifyCache() throws Exception {
		Product product = new Product();
		product.setName("Phone");
		product.setPrice(BigDecimal.valueOf(800L));
		product = productRepository.save(product);

		// Lấy lần 1: từ DB
		mockMvc.perform(get("/api/product/" + product.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Phone"));

		// Lấy lần 2: từ cache
		mockMvc.perform(get("/api/product/" + product.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Phone"));
	}

	@Test
	void testUpdateProductAndVerifyCache() throws Exception {
		// Bước 1: Tạo và lưu Product ban đầu
		Product product = new Product();
		product.setName("Tablet");
		product.setPrice(BigDecimal.valueOf(500L));
		product = productRepository.save(product);

		// Bước 2: Tạo DTO ProductRequest để gửi request cập nhật
		ProductRequest updateRequest = new ProductRequest();
		updateRequest.setName("Updated Tablet");
		updateRequest.setPrice(BigDecimal.valueOf(550L));

		// Bước 3: Gửi PUT request cập nhật Product
		mockMvc.perform(MockMvcRequestBuilders.put("/api/product/" + product.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Tablet"))
				.andExpect(jsonPath("$.price").value(550.0));

		// Bước 4: Kiểm tra cache đã được cập nhật
		Cache cache = cacheManager.getCache(ProductServiceImpl.PRODUCT_CACHE);
		assertNotNull(cache, "Cache không được null, hãy kiểm tra cấu hình cache");

		ProductResponse cachedProduct = cache.get(product.getId(), ProductResponse.class);
		assertNotNull(cachedProduct, "Product trong cache không được null, có thể @CachePut chưa chạy");

		Assertions.assertEquals("Updated Tablet", cachedProduct.getName(),
				"Tên sản phẩm trong cache không đúng");
		Assertions.assertEquals(BigDecimal.valueOf(550L), cachedProduct.getPrice(),
				"Giá sản phẩm trong cache không đúng");
	}


	@Test
	void testDeleteProductAndEvictCache() throws Exception {
		Product product = new Product();
		product.setName("Smartwatch");
		product.setPrice(BigDecimal.valueOf(250L));
		product = productRepository.save(product);

		mockMvc.perform(delete("/api/product/" + product.getId()))
				.andExpect(status().isNoContent());

		Assertions.assertFalse(productRepository.findById(product.getId()).isPresent());

		Cache cache = cacheManager.getCache(ProductServiceImpl.PRODUCT_CACHE);
		assertNotNull(cache);

		ProductResponse cached = cache.get(product.getId(), ProductResponse.class);
		Assertions.assertNull(cached); // Đảm bảo đã xóa khỏi cache
	}
}

