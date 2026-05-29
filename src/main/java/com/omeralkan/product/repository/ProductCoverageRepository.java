package com.omeralkan.product.repository;

import com.omeralkan.product.entity.ProductCoverageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductCoverageRepository extends JpaRepository<ProductCoverageEntity, Long> {
    // Ürüne göre teminatları bulmak için özel bir metod ekleyelim
    List<ProductCoverageEntity> findByProductId(Long productId);
}