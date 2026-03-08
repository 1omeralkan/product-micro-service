package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.repository.ProductRepository;
import com.omeralkan.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final String PRODUCT_NOT_FOUND_MSG = "Ürün bulunamadı. ID: ";

    @Override
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        ProductEntity entity = new ProductEntity();
        entity.setProductCode(requestDto.getProductCode());
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());

        if (requestDto.getCategoryIds() != null && !requestDto.getCategoryIds().isEmpty()) {
            Set<CategoryEntity> categories = new HashSet<>(categoryRepository.findAllById(requestDto.getCategoryIds()));
            entity.setCategories(categories);
        }

        ProductEntity savedEntity = productRepository.save(entity);
        return mapToResponse(savedEntity);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_MSG + id));
        return mapToResponse(entity);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_MSG + id));

        entity.setProductCode(requestDto.getProductCode());
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());

        if (requestDto.getCategoryIds() != null) {
            Set<CategoryEntity> categories = new HashSet<>(categoryRepository.findAllById(requestDto.getCategoryIds()));
            entity.setCategories(categories);
        } else {
            entity.getCategories().clear();
        }

        ProductEntity updatedEntity = productRepository.save(entity);
        return mapToResponse(updatedEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_MSG + id));

        entity.setIsActive(false);
        productRepository.save(entity);
    }

    private ProductResponseDto mapToResponse(ProductEntity entity) {
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setProductCode(entity.getProductCode());
        responseDto.setName(entity.getName());
        responseDto.setDescription(entity.getDescription());
        responseDto.setIsActive(entity.getIsActive());

        if (entity.getCategories() != null) {
            Set<CategoryResponseDto> categoryDtos = entity.getCategories().stream()
                    .map(this::mapCategoryToResponse)
                    .collect(Collectors.toSet());
            responseDto.setCategories(categoryDtos);
        }

        return responseDto;
    }

    private CategoryResponseDto mapCategoryToResponse(CategoryEntity entity) {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setDescription(entity.getDescription());
        responseDto.setIsActive(entity.getIsActive());
        return responseDto;
    }
}