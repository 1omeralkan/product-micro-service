package com.omeralkan.product.repository;

import com.omeralkan.product.entity.ProductAmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAmountRepository extends JpaRepository<ProductAmountEntity, Long> {

    List<ProductAmountEntity> findAllByProductIdAndIsActiveTrue(Long productId);

    Optional<ProductAmountEntity> findByProductIdAndExpiryDateIsNullAndIsActiveTrue(Long productId);

    List<ProductAmountEntity> findAllByIsActiveTrue();
}