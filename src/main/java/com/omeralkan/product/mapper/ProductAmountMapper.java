package com.omeralkan.product.mapper;

import com.omeralkan.product.dto.request.ProductAmountRequestDto;
import com.omeralkan.product.dto.response.ProductAmountResponseDto;
import com.omeralkan.product.entity.ProductAmountEntity;
import com.omeralkan.product.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductAmountMapper {

    public ProductAmountResponseDto toResponse(ProductAmountEntity entity) {
        ProductAmountResponseDto dto = new ProductAmountResponseDto();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setAmount(entity.getAmount());
        dto.setEffectiveDate(entity.getEffectiveDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public ProductAmountEntity toEntity(ProductAmountRequestDto requestDto, ProductEntity product) {
        ProductAmountEntity entity = new ProductAmountEntity();
        entity.setProduct(product);
        entity.setAmount(requestDto.getAmount());
        entity.setEffectiveDate(requestDto.getEffectiveDate());
        return entity;
    }
}