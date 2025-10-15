package com.dophuong.demo_redis.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {
    @NotBlank(message = "SKU không được để trống")
    private String sku;

    @NotBlank(message = "Tên sản phẩm không đucợ để trống")
    private String name;

    @NotNull(message = "Không đucợ để trống giá tiền")
    private BigDecimal price;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}
