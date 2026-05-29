package com.omeralkan.product.service;

import com.omeralkan.product.dto.request.ProductCoverageRequestDto;
import java.util.List;

public interface ProductCoverageService {
    void addCoverageToProduct(ProductCoverageRequestDto request);
    List<ProductCoverageRequestDto> getCoveragesByProductId(Long productId); // Listeleme
    void updateCoverage(Long id, ProductCoverageRequestDto request); // Güncelleme
    void deleteCoverage(Long id); // Silme
}