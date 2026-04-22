package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.exception.ErrorCodes;
import com.omeralkan.product.mapper.ProductMapper;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.repository.ProductRepository;
import com.omeralkan.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;



    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        ProductEntity entity = productMapper.toEntity(requestDto);
        assignCategories(entity, requestDto.getCategoryIds());
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toResponse(savedEntity);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        ProductEntity entity = findActiveByIdOrThrow(id);
        return productMapper.toResponse(entity);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllByIsActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        ProductEntity entity = findActiveByIdOrThrow(id);
        productMapper.updateEntityFromDto(requestDto, entity);
        assignCategories(entity, requestDto.getCategoryIds());
        ProductEntity updatedEntity = productRepository.save(entity);
        return productMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity entity = findActiveByIdOrThrow(id);
        entity.setIsActive(false);
        productRepository.save(entity);
    }

    private ProductEntity findActiveByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .filter(ProductEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(ErrorCodes.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private void assignCategories(ProductEntity entity, Set<Long> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<CategoryEntity> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
            entity.setCategories(categories);
        } else {
            entity.getCategories().clear();
        }
    }
}