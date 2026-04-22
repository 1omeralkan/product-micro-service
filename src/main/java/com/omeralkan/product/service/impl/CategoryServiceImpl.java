package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.exception.ErrorCodes;
import com.omeralkan.product.mapper.CategoryMapper;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        CategoryEntity entity = categoryMapper.toEntity(requestDto);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        return categoryMapper.toResponse(savedEntity);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        CategoryEntity entity = findActiveByIdOrThrow(id);
        return categoryMapper.toResponse(entity);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAllByIsActiveTrue()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        CategoryEntity entity = findActiveByIdOrThrow(id);
        categoryMapper.updateEntityFromDto(requestDto, entity);
        CategoryEntity updatedEntity = categoryRepository.save(entity);
        return categoryMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity entity = findActiveByIdOrThrow(id);
        entity.setIsActive(false);
        categoryRepository.save(entity);
    }

    private CategoryEntity findActiveByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .filter(CategoryEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}