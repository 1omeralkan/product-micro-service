package com.omeralkan.product.mapper;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.dto.response.ProductCoverageResponseDto; // EKLENDİ
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.entity.ProductCoverageEntity; // EKLENDİ
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

        // GERÇEK ENTEGRASYON: Teminatların DTO'ya dönüştürülüp listeye eklenmesi
        if (entity.getCoverages() != null) {
            dto.setCoverages(
                    entity.getCoverages().stream()
                            .map(this::toCoverageResponse)
                            .collect(Collectors.toList())
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

    private ProductCoverageResponseDto toCoverageResponse(ProductCoverageEntity coverageEntity) {
        ProductCoverageResponseDto dto = new ProductCoverageResponseDto();
        dto.setId(coverageEntity.getId());
        dto.setCoverageCode(coverageEntity.getCoverageCode());
        dto.setName(coverageEntity.getName());
        dto.setMinAmount(coverageEntity.getMinAmount());
        dto.setMaxAmount(coverageEntity.getMaxAmount());
        return dto;
    }
}