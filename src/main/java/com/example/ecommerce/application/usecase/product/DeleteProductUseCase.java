package com.example.ecommerce.application.usecase.product;

import com.example.ecommerce.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {
    private final ProductRepository productRepository;

    @Transactional
    public void execute(UUID id) {
        if (!productRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
} 