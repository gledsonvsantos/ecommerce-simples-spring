package com.example.ecommerce.presentation.mapper;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.presentation.dto.CreateProductRequest;
import com.example.ecommerce.presentation.dto.ProductResponse;
import com.example.ecommerce.presentation.dto.UpdateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    @Mapping(target = "id", ignore = true)
    Product toDomain(CreateProductRequest request);
    
    @Mapping(target = "id", ignore = true)
    Product toDomain(UpdateProductRequest request);
    
    ProductResponse toResponse(Product domain);
} 