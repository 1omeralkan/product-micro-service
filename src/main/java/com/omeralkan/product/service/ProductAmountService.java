package com.omeralkan.product.service;

import com.omeralkan.product.dto.request.ProductAmountRequestDto;
import com.omeralkan.product.dto.response.ProductAmountResponseDto;

import java.util.List;

public interface ProductAmountService {

    ProductAmountResponseDto createProductAmount(ProductAmountRequestDto requestDto);

    ProductAmountResponseDto getProductAmountById(Long id);

    List<ProductAmountResponseDto> getAllProductAmounts();

    List<ProductAmountResponseDto> getProductAmountsByProductId(Long productId);

    ProductAmountResponseDto getActiveAmountByProductId(Long productId);

    void deleteProductAmount(Long id);
}