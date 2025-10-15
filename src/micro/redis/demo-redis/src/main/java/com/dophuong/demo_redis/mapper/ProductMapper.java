package com.dophuong.demo_redis.mapper;

import com.dophuong.demo_redis.dto.request.ProductCreateRequest;
import com.dophuong.demo_redis.dto.response.ProductResponse;
import com.dophuong.demo_redis.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductCreateRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    ProductResponse toResponse(Product product);
}
