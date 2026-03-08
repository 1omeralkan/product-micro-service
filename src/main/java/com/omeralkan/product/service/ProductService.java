package com.omeralkan.product.service;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto requestDto);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllProducts();
    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);
    void deleteProduct(Long id);
}