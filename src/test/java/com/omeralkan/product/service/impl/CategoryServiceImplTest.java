package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.CategoryRequestDto;
import com.omeralkan.product.dto.response.CategoryResponseDto;
import com.omeralkan.product.entity.CategoryEntity;
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

@ExtendWith(MockitoExtension.class) // Mockito'yu devreye sokuyoruz
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository; // Veritabanını taklit ediyoruz (Mock)

    @InjectMocks
    private CategoryServiceImpl categoryService; // Test edeceğimiz asıl servis

    @Test
    void createCategory_ShouldReturnCategoryResponseDto() {
        // 1. GIVEN (Hazırlık Aşaması)
        CategoryRequestDto requestDto = new CategoryRequestDto("Elektronik", "Test Açıklama");

        CategoryEntity savedEntity = new CategoryEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Elektronik");
        savedEntity.setDescription("Test Açıklama");
        savedEntity.setIsActive(true);

        // "Repository'nin save metodu çağrılırsa ona şu sahte entity'yi dön" diyoruz
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);

        // 2. WHEN (Eylem Aşaması - Metodu tetikliyoruz)
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto);

        // 3. THEN (Doğrulama Aşaması)
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("Elektronik", responseDto.getName());

        // Save metodunun tam olarak 1 kere çağırıldığını teyit ediyoruz (Kalite kontrol!)
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategoryResponseDto() {
        // GIVEN
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        // WHEN
        CategoryResponseDto responseDto = categoryService.getCategoryById(1L);

        // THEN
        assertNotNull(responseDto);
        assertEquals("Elektronik", responseDto.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // GIVEN: Veritabanında ürün yokmuş gibi davran (Optional.empty dön)
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN: Hata fırlatmasını bekliyoruz
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Kategori bulunamadı. ID: 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }
}