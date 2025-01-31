package com.example.ecommerce.application.usecase.product;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {
    private final ProductRepository productRepository;

    @Transactional
    public Product execute(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }
        return productRepository.save(product);
    }
} 