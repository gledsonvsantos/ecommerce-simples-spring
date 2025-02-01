package com.example.ecommerce.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricsAspect {

    private final MeterRegistry registry;

    public MetricsAspect(MeterRegistry registry) {
        this.registry = registry;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object measureControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(registry);
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            return joinPoint.proceed();
        } finally {
            sample.stop(Timer.builder("http.server.requests.custom")
                    .tag("class", className)
                    .tag("method", methodName)
                    .description("Custom metrics for REST controllers")
                    .register(registry));
        }
    }
} 