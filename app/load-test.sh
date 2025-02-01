#!/bin/bash

# Função para fazer requisições GET
function make_get_requests() {
    for i in {1..50}; do
        curl -s "http://localhost:8080/api/v1/products" > /dev/null &
        
        # Adicionar GETs com IDs inválidos para gerar 404
        if [ $((i % 5)) -eq 0 ]; then
            curl -s "http://localhost:8080/api/v1/products/$((RANDOM % 9999))" > /dev/null &
        fi
        sleep 0.2
    done
}

# Função para fazer requisições POST
function make_post_requests() {
    for i in {1..10}; do
        # POST válido
        curl -s -X POST "http://localhost:8080/api/v1/products" \
        -H "Content-Type: application/json" \
        -d "{\"name\":\"Product $i\",\"description\":\"Test product $i\",\"price\":99.99,\"sku\":\"SKU-$i\",\"stockQuantity\":100}" > /dev/null &
        
        # POST inválido (sem campos obrigatórios)
        if [ $((i % 2)) -eq 0 ]; then
            curl -s -X POST "http://localhost:8080/api/v1/products" \
            -H "Content-Type: application/json" \
            -d "{\"name\":\"Invalid Product\"}" > /dev/null &
        fi
        
        sleep 0.5
    done
}

# Função para gerar erros 400 e 500
function generate_error_requests() {
    # Requisições mal formatadas (400)
    curl -s -X POST "http://localhost:8080/api/v1/products" \
    -H "Content-Type: application/json" \
    -d "{invalid_json" > /dev/null &
    
    # Tentativa de DELETE em endpoint inexistente (405)
    curl -s -X DELETE "http://localhost:8080/api/v1/products/invalid" > /dev/null &
    
    # Requisições para endpoints inexistentes (404)
    curl -s "http://localhost:8080/api/v1/invalid-endpoint" > /dev/null &
    curl -s "http://localhost:8080/api/v1/products/99999999" > /dev/null &
    
    # POST com payload muito grande
    local large_payload=""
    for i in {1..1000}; do
        large_payload="$large_payload,\"field$i\":\"value$i\""
    done
    curl -s -X POST "http://localhost:8080/api/v1/products" \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"Large Product\"$large_payload}" > /dev/null &
}

# Função para fazer requisições variadas
function generate_mixed_traffic() {
    echo "Gerando tráfego variado com mais cenários de erro..."
    
    # Loop principal
    for i in {1..5}; do
        # Gerar GET requests em paralelo
        make_get_requests &
        
        # Gerar alguns POST requests
        make_post_requests &
        
        # Gerar requisições com erro
        generate_error_requests &
        
        # Adicionar requisições concorrentes para aumentar a carga
        for j in {1..3}; do
            curl -s "http://localhost:8080/api/v1/products" > /dev/null &
            curl -s "http://localhost:8080/api/v1/products/1" > /dev/null &
        done
        
        sleep 2
    done
    
    echo "Tráfego gerado com sucesso!"
}

# Executar o gerador de tráfego
generate_mixed_traffic 