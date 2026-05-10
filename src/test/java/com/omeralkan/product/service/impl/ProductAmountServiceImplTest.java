package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductAmountRequestDto;
import com.omeralkan.product.dto.response.ProductAmountResponseDto;
import com.omeralkan.product.entity.ProductAmountEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.mapper.ProductAmountMapper;
import com.omeralkan.product.repository.ProductAmountRepository;
import com.omeralkan.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAmountServiceImplTest {

    @Mock
    private ProductAmountRepository productAmountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAmountMapper productAmountMapper;

    @InjectMocks
    private ProductAmountServiceImpl productAmountService;


    @Test
    void createProductAmount_WhenNoActiveAmount_ShouldCreateSuccessfully() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setIsActive(true);

        ProductAmountRequestDto requestDto = new ProductAmountRequestDto();
        requestDto.setProductId(1L);
        requestDto.setAmount(new BigDecimal("50000"));
        requestDto.setEffectiveDate(LocalDate.of(2026, 1, 1));

        ProductAmountEntity entity = new ProductAmountEntity();
        ProductAmountEntity savedEntity = new ProductAmountEntity();
        savedEntity.setId(1L);

        ProductAmountResponseDto responseDto = new ProductAmountResponseDto();
        responseDto.setId(1L);
        responseDto.setAmount(new BigDecimal("50000"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productAmountRepository.findByProductIdAndExpiryDateIsNullAndIsActiveTrue(1L))
                .thenReturn(Optional.empty());
        when(productAmountMapper.toEntity(requestDto, product)).thenReturn(entity);
        when(productAmountRepository.save(any(ProductAmountEntity.class))).thenReturn(savedEntity);
        when(productAmountMapper.toResponse(savedEntity)).thenReturn(responseDto);

        ProductAmountResponseDto result = productAmountService.createProductAmount(requestDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("50000"), result.getAmount());
        verify(productAmountRepository, times(1)).save(any(ProductAmountEntity.class));
    }


    @Test
    void createProductAmount_WhenActiveAmountExists_ShouldCloseOldAndCreateNew() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setIsActive(true);

        ProductAmountEntity oldActiveAmount = new ProductAmountEntity();
        oldActiveAmount.setId(1L);
        oldActiveAmount.setAmount(new BigDecimal("50000"));
        oldActiveAmount.setExpiryDate(null);

        ProductAmountRequestDto requestDto = new ProductAmountRequestDto();
        requestDto.setProductId(1L);
        requestDto.setAmount(new BigDecimal("55000"));
        requestDto.setEffectiveDate(LocalDate.of(2026, 4, 1));

        ProductAmountEntity newEntity = new ProductAmountEntity();
        ProductAmountEntity savedNewEntity = new ProductAmountEntity();
        savedNewEntity.setId(2L);

        ProductAmountResponseDto responseDto = new ProductAmountResponseDto();
        responseDto.setId(2L);
        responseDto.setAmount(new BigDecimal("55000"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productAmountRepository.findByProductIdAndExpiryDateIsNullAndIsActiveTrue(1L))
                .thenReturn(Optional.of(oldActiveAmount));
        when(productAmountRepository.save(oldActiveAmount)).thenReturn(oldActiveAmount);
        when(productAmountMapper.toEntity(requestDto, product)).thenReturn(newEntity);
        when(productAmountRepository.save(newEntity)).thenReturn(savedNewEntity);
        when(productAmountMapper.toResponse(savedNewEntity)).thenReturn(responseDto);

        ProductAmountResponseDto result = productAmountService.createProductAmount(requestDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("55000"), result.getAmount());
        assertEquals(LocalDate.of(2026, 4, 1), oldActiveAmount.getExpiryDate());
        verify(productAmountRepository, times(2)).save(any(ProductAmountEntity.class));
    }


    @Test
    void createProductAmount_WhenProductNotFound_ShouldThrowBusinessException() {
        ProductAmountRequestDto requestDto = new ProductAmountRequestDto();
        requestDto.setProductId(999L);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productAmountService.createProductAmount(requestDto);
        });

        assertEquals("PROD-404", exception.getMessage());
        verify(productAmountRepository, never()).save(any(ProductAmountEntity.class));
    }


    @Test
    void createProductAmount_WhenProductIsSoftDeleted_ShouldThrowBusinessException() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setIsActive(false);

        ProductAmountRequestDto requestDto = new ProductAmountRequestDto();
        requestDto.setProductId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productAmountService.createProductAmount(requestDto);
        });

        assertEquals("PROD-404", exception.getMessage());
        verify(productAmountRepository, never()).save(any(ProductAmountEntity.class));
    }


    @Test
    void deleteProductAmount_ShouldSetIsActiveFalse() {
        ProductAmountEntity entity = new ProductAmountEntity();
        entity.setId(1L);
        entity.setAmount(new BigDecimal("50000"));
        entity.setIsActive(true);

        when(productAmountRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productAmountRepository.save(any(ProductAmountEntity.class))).thenReturn(entity);

        productAmountService.deleteProductAmount(1L);

        assertFalse(entity.getIsActive());
        verify(productAmountRepository, times(1)).save(entity);
    }
}