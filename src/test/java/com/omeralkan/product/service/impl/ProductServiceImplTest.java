package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.mapper.ProductMapper;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void createProduct_ShouldReturnProductResponseDto_WithCategories() {
        Set<Long> categoryIds = new HashSet<>(List.of(1L));
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setProductCode("TEL-001");
        requestDto.setName("iPhone 15");
        requestDto.setDescription("Apple Telefon");
        requestDto.setCategoryIds(categoryIds);

        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Elektronik");

        ProductEntity entity = new ProductEntity();
        entity.setProductCode("TEL-001");
        entity.setName("iPhone 15");

        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(1L);
        savedEntity.setProductCode("TEL-001");
        savedEntity.setName("iPhone 15");
        savedEntity.setIsActive(true);
        savedEntity.setCategories(new HashSet<>(List.of(category)));

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setName("Elektronik");

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setProductCode("TEL-001");
        responseDto.setName("iPhone 15");
        responseDto.setCategories(new HashSet<>(List.of(categoryResponseDto)));

        when(productMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.findAllById(anyIterable())).thenReturn(List.of(category));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);
        when(productMapper.toResponse(savedEntity)).thenReturn(responseDto);

        ProductResponseDto result = productService.createProduct(requestDto);

        assertNotNull(result);
        assertEquals("TEL-001", result.getProductCode());
        assertEquals(1, result.getCategories().size());

        verify(productMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).findAllById(anyIterable());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
        verify(productMapper, times(1)).toResponse(savedEntity);
    }


    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponseDto() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setProductCode("TEL-001");
        entity.setName("iPhone 15");
        entity.setIsActive(true);

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setProductCode("TEL-001");

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productMapper.toResponse(entity)).thenReturn(responseDto);

        ProductResponseDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("TEL-001", result.getProductCode());
        verify(productRepository, times(1)).findById(1L);
    }


    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowBusinessException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("PROD-404", exception.getMessage());
    }


    @Test
    void getProductById_WhenProductIsSoftDeleted_ShouldThrowBusinessException() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setIsActive(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("PROD-404", exception.getMessage());
    }


    @Test
    void deleteProduct_ShouldSetIsActiveFalse() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("iPhone 15");
        entity.setIsActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(entity);

        productService.deleteProduct(1L);

            assertFalse(entity.getIsActive());
        verify(productRepository, times(1)).save(entity);
    }
}