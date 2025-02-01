package com.example.ecommerce.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BusinessMetrics {

    private final Counter ordersCreatedCounter;
    private final Counter ordersCancelledCounter;
    private final Counter productsCreatedCounter;
    private final Counter productsOutOfStockCounter;

    public BusinessMetrics(MeterRegistry registry) {
        this.ordersCreatedCounter = Counter.builder("business.orders.created")
                .description("Number of orders created")
                .register(registry);

        this.ordersCancelledCounter = Counter.builder("business.orders.cancelled")
                .description("Number of orders cancelled")
                .register(registry);

        this.productsCreatedCounter = Counter.builder("business.products.created")
                .description("Number of products created")
                .register(registry);

        this.productsOutOfStockCounter = Counter.builder("business.products.out_of_stock")
                .description("Number of times products went out of stock")
                .register(registry);
    }

    public void incrementOrdersCreated() {
        ordersCreatedCounter.increment();
    }

    public void incrementOrdersCancelled() {
        ordersCancelledCounter.increment();
    }

    public void incrementProductsCreated() {
        productsCreatedCounter.increment();
    }

    public void incrementProductsOutOfStock() {
        productsOutOfStockCounter.increment();
    }
} 