package com.omeralkan.product.mapper;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto toResponse(CategoryEntity entity) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public CategoryEntity toEntity(CategoryRequestDto requestDto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
        return entity;
    }

    public void updateEntityFromDto(CategoryRequestDto requestDto, CategoryEntity entity) {
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
    }
}