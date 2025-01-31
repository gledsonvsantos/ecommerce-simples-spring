package com.example.ecommerce.infrastructure.persistence.mapper;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product domain);
} 