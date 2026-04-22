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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Mockito'yu devreye sok — gerçek veritabanı kullanmadan test yapıyoruz
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    // @Mock: Bu nesneler SAHTE (fake) — gerçek veritabanına gitmiyor
    // "categoryRepository.save() çağrılırsa şu sahte veriyi dön" diye kurallar koyuyoruz
    @Mock
    private CategoryRepository categoryRepository;

    // ESKİDEN YOKTU — Refactor'da CategoryMapper ekledik, şimdi mock'lamamız lazım
    // Mock'lamazsak NullPointerException alırız çünkü Spring yok, inject edilemiyor
    @Mock
    private CategoryMapper categoryMapper;

    // @InjectMocks: Test edeceğimiz GERÇEK sınıf
    // Yukarıdaki @Mock'ları otomatik olarak bu sınıfa inject eder
    @InjectMocks
    private CategoryServiceImpl categoryService;

    // ==================== CREATE ====================

    @Test
    void createCategory_ShouldReturnCategoryResponseDto() {
        // GIVEN — test verilerini hazırla
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

        // Mock kuralları: "bu metod çağrılırsa şunu dön"
        when(categoryMapper.toEntity(requestDto)).thenReturn(entity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedEntity);
        when(categoryMapper.toResponse(savedEntity)).thenReturn(responseDto);

        // WHEN — test edilecek metodu çağır
        CategoryResponseDto result = categoryService.createCategory(requestDto);

        // THEN — sonuçları doğrula
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Elektronik", result.getName());

        // Metodların doğru sayıda çağrıldığını teyit et
        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(categoryMapper, times(1)).toResponse(savedEntity);
    }

    // ==================== GET BY ID — BAŞARILI ====================

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategoryResponseDto() {
        // GIVEN
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(true); // ÖNEMLİ: isActive=true olmalı, yoksa filter eler

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Elektronik");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapper.toResponse(entity)).thenReturn(responseDto);

        // WHEN
        CategoryResponseDto result = categoryService.getCategoryById(1L);

        // THEN
        assertNotNull(result);
        assertEquals("Elektronik", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    // ==================== GET BY ID — BULUNAMADI ====================

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowBusinessException() {
        // GIVEN — veritabanında kayıt yok
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        // ESKİDEN: RuntimeException bekliyorduk
        // ŞİMDİ: BusinessException bekliyoruz — refactor'da değiştirdik
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        // Error code doğru mu kontrol et
        assertEquals("PROD-CAT-404", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    // ==================== GET BY ID — SOFT DELETE EDİLMİŞ ====================

    @Test
    void getCategoryById_WhenCategoryIsSoftDeleted_ShouldThrowBusinessException() {
        // GIVEN — kayıt var ama isActive=false (soft-delete edilmiş)
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(false); // Pasif

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        // WHEN & THEN — pasif kayıt da "bulunamadı" hatası vermeli
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("PROD-CAT-404", exception.getMessage());
    }

    // ==================== DELETE ====================

    @Test
    void deleteCategory_ShouldSetIsActiveFalse() {
        // GIVEN
        CategoryEntity entity = new CategoryEntity();
        entity.setId(1L);
        entity.setName("Elektronik");
        entity.setIsActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(entity);

        // WHEN
        categoryService.deleteCategory(1L);

        // THEN — isActive false olmuş mu?
        assertFalse(entity.getIsActive());
        verify(categoryRepository, times(1)).save(entity);
    }
}