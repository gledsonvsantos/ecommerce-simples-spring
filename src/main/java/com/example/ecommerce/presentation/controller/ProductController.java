package com.example.ecommerce.presentation.controller;

import com.example.ecommerce.application.usecase.product.*;
import com.example.ecommerce.presentation.dto.CreateProductRequest;
import com.example.ecommerce.presentation.dto.ProductResponse;
import com.example.ecommerce.presentation.dto.UpdateProductRequest;
import com.example.ecommerce.presentation.mapper.ProductDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductDtoMapper productDtoMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        var product = productDtoMapper.toDomain(request);
        var createdProduct = createProductUseCase.execute(product);
        return new ResponseEntity<>(productDtoMapper.toResponse(createdProduct), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        var product = getProductUseCase.execute(id);
        return ResponseEntity.ok(productDtoMapper.toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        var products = listProductsUseCase.execute();
        var response = products.stream()
                .map(productDtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        var product = productDtoMapper.toDomain(request);
        var updatedProduct = updateProductUseCase.execute(id, product);
        return ResponseEntity.ok(productDtoMapper.toResponse(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
} 