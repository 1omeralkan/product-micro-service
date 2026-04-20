package com.omeralkan.product.repository;

import com.omeralkan.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductCode(String productCode);

    List<ProductEntity> findAllByIsActiveTrue();
}