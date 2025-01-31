package com.example.ecommerce.application.usecase.product;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProductUseCase {
    private final ProductRepository productRepository;

    public Product execute(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }
} 