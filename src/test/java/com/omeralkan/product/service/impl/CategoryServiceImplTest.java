package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.mapper.CategoryMapper;
import com.omeralkan.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_ShouldReturnCategoryResponseDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Elektronik");
        requestDto.setDescription("Test Açıklama");

        CategoryEntity entity = new CategoryEntity();
        entity.setName("Elektronik");
        entity.setDescription("Test Açıklama");

        CategoryEntity savedEntity = new CategoryEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Elektronik");
        savedEntity.setDescription("Test Açıklama");
        savedEntity.setIsActive(true);

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Elektronik");
        responseDto.setDescription("Test Açıklama");
        responseDto.setIsActive(true);

        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);
        when(categoryMapper.toResponse(savedEntity)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.createCategory(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Elektronik", result.getName());

        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(categoryMapper, times(1)).toResponse(savedEntity);
    }


    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategoryResponseDto() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(true);

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Elektronik");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapper.toResponse(entity)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Elektronik", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }


    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowBusinessException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("PROD-CAT-404", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }


    @Test
    void getCategoryById_WhenCategoryIsSoftDeleted_ShouldThrowBusinessException() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(false); // Pasif

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("PROD-CAT-404", exception.getMessage());
    }


    @Test
    void deleteCategory_ShouldSetIsActiveFalse() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(entity);

        categoryService.deleteCategory(1L);

        assertFalse(entity.getIsActive());
        verify(categoryRepository, times(1)).save(entity);
    }
}