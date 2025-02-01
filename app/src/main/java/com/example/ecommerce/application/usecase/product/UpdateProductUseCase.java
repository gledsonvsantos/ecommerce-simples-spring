package com.example.ecommerce.application.usecase.product;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProductRepository productRepository;

    @Transactional
    public Product execute(UUID id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if (!existingProduct.getSku().equals(updatedProduct.getSku()) &&
            productRepository.existsBySku(updatedProduct.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + updatedProduct.getSku() + " already exists");
        }

        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }
} 