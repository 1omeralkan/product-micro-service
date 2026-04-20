package com.omeralkan.product.mapper;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductResponseDto toResponse(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(entity.getId());
        dto.setProductCode(entity.getProductCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());

        if (entity.getCategories() != null) {
            dto.setCategories(
                    entity.getCategories().stream()
                            .map(categoryMapper::toResponse)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public ProductEntity toEntity(ProductRequestDto requestDto) {
        ProductEntity entity = new ProductEntity();
        entity.setProductCode(requestDto.getProductCode());
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
        return entity;
    }

    public void updateEntityFromDto(ProductRequestDto requestDto, ProductEntity entity) {
        entity.setProductCode(requestDto.getProductCode());
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
    }
}