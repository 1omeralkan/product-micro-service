package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductRequestDto;
import com.omeralkan.product.dto.response.ProductResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.repository.CategoryRepository;
import com.omeralkan.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository; // Ürün veritabanını taklit et

    @Mock
    private CategoryRepository categoryRepository; // Kategori veritabanını da taklit et (İlişki için şart)

    @InjectMocks
    private ProductServiceImpl productService; // Test edilecek asıl beyin

    @Test
    void createProduct_ShouldReturnProductResponseDto_WithCategories() {
        // 1. GIVEN (Hazırlık)
        Set<Long> categoryIds = new HashSet<>(List.of(1L));
        ProductRequestDto requestDto = new ProductRequestDto("TEL-001", "iPhone 15", "Apple Telefon", categoryIds);

        // Sahte Kategori
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Elektronik");

        // Sahte Kaydedilmiş Ürün
        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(1L);
        savedEntity.setProductCode("TEL-001");
        savedEntity.setName("iPhone 15");
        savedEntity.setCategories(new HashSet<>(List.of(category)));

        // Kurallarımızı belirliyoruz: "Bu metodlar çağrıldığında bu sahte verileri dön"
        when(categoryRepository.findAllById(anyIterable())).thenReturn(List.of(category));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        // 2. WHEN (Eylem)
        ProductResponseDto responseDto = productService.createProduct(requestDto);

        // 3. THEN (Doğrulama)
        assertNotNull(responseDto);
        assertEquals("TEL-001", responseDto.getProductCode());
        assertEquals(1, responseDto.getCategories().size()); // Kategorisi de gelmiş mi?

        // Şov kısmı: İlgili repository metodları tam 1 kere çağrıldı mı diye teyit ediyoruz
        verify(categoryRepository, times(1)).findAllById(anyIterable());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponseDto() {
        // GIVEN
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setProductCode("TEL-001");
        entity.setName("iPhone 15");

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));

        // WHEN
        ProductResponseDto responseDto = productService.getProductById(1L);

        // THEN
        assertNotNull(responseDto);
        assertEquals("TEL-001", responseDto.getProductCode());
        verify(productRepository, times(1)).findById(1L);
    }
}