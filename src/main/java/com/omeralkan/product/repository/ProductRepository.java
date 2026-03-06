package com.omeralkan.product.repository;

import com.omeralkan.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // Ürün koduna (product_code) göre veritabanında arama yapan efsane metod
    Optional<ProductEntity> findByProductCode(String productCode);

    // Sadece aktif ürünleri getiren listeleme metodu
    List<ProductEntity> findAllByIsActiveTrue();
}