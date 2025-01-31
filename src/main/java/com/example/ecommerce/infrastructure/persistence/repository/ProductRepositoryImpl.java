package com.example.ecommerce.infrastructure.persistence.repository;

import com.example.ecommerce.domain.model.Product;
import com.example.ecommerce.domain.repository.ProductRepository;
import com.example.ecommerce.infrastructure.persistence.entity.ProductEntity;
import com.example.ecommerce.infrastructure.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = jpaProductRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaProductRepository.deleteById(id);
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaProductRepository.existsBySku(sku);
    }
} 