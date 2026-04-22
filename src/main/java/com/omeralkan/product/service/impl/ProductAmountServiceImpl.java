package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductAmountRequestDto;
import com.omeralkan.product.dto.response.ProductAmountResponseDto;
import com.omeralkan.product.entity.ProductAmountEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.exception.BusinessException;
import com.omeralkan.product.exception.ErrorCodes;
import com.omeralkan.product.mapper.ProductAmountMapper;
import com.omeralkan.product.repository.ProductAmountRepository;
import com.omeralkan.product.repository.ProductRepository;
import com.omeralkan.product.service.ProductAmountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAmountServiceImpl implements ProductAmountService {

    private final ProductAmountRepository productAmountRepository;
    private final ProductRepository productRepository;
    private final ProductAmountMapper productAmountMapper;


    @Override
    @Transactional
    public ProductAmountResponseDto createProductAmount(ProductAmountRequestDto requestDto) {
        ProductEntity product = findActiveProductOrThrow(requestDto.getProductId());

        closeActiveAmountIfExists(product.getId(), requestDto.getEffectiveDate());

        ProductAmountEntity entity = productAmountMapper.toEntity(requestDto, product);
        ProductAmountEntity savedEntity = productAmountRepository.save(entity);

        log.info("Yeni fiyat eklendi. Ürün: {}, Fiyat: {}, Geçerlilik: {}",
                product.getName(), requestDto.getAmount(), requestDto.getEffectiveDate());

        return productAmountMapper.toResponse(savedEntity);
    }

    @Override
    public ProductAmountResponseDto getProductAmountById(Long id) {
        ProductAmountEntity entity = findActiveAmountOrThrow(id);
        return productAmountMapper.toResponse(entity);
    }

    @Override
    public List<ProductAmountResponseDto> getAllProductAmounts() {
        return productAmountRepository.findAllByIsActiveTrue()
                .stream()
                .map(productAmountMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductAmountResponseDto> getProductAmountsByProductId(Long productId) {
        findActiveProductOrThrow(productId);

        return productAmountRepository.findAllByProductIdAndIsActiveTrue(productId)
                .stream()
                .map(productAmountMapper::toResponse)
                .toList();
    }

    @Override
    public ProductAmountResponseDto getActiveAmountByProductId(Long productId) {
        findActiveProductOrThrow(productId);

        ProductAmountEntity entity = productAmountRepository
                .findByProductIdAndExpiryDateIsNullAndIsActiveTrue(productId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ACTIVE_AMOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));

        return productAmountMapper.toResponse(entity);
    }

    @Override
    public void deleteProductAmount(Long id) {
        ProductAmountEntity entity = findActiveAmountOrThrow(id);
        entity.setIsActive(false);
        productAmountRepository.save(entity);

        log.info("Fiyat kaydı silindi. ID: {}", id);
    }

    private ProductEntity findActiveProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .filter(ProductEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(ErrorCodes.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private ProductAmountEntity findActiveAmountOrThrow(Long id) {
        return productAmountRepository.findById(id)
                .filter(ProductAmountEntity::getIsActive)
                .orElseThrow(() -> new BusinessException(ErrorCodes.AMOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private void closeActiveAmountIfExists(Long productId, LocalDate newEffectiveDate) {
        productAmountRepository
                .findByProductIdAndExpiryDateIsNullAndIsActiveTrue(productId)
                .ifPresent(activeAmount -> {
                    activeAmount.setExpiryDate(newEffectiveDate);
                    productAmountRepository.save(activeAmount);

                    log.info("Eski aktif fiyat kapatıldı. ID: {}, Bitiş: {}",
                            activeAmount.getId(), newEffectiveDate);
                });
    }
}