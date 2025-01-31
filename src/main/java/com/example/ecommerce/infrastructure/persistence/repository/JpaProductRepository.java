package com.example.ecommerce.infrastructure.persistence.repository;

import com.example.ecommerce.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsBySku(String sku);
} 