package com.omeralkan.product.service.impl;

import com.omeralkan.product.dto.request.ProductCoverageRequestDto;
import com.omeralkan.product.entity.ProductCoverageEntity;
import com.omeralkan.product.entity.ProductEntity;
import com.omeralkan.product.repository.ProductCoverageRepository;
import com.omeralkan.product.repository.ProductRepository;
import com.omeralkan.product.service.ProductCoverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCoverageServiceImpl implements ProductCoverageService {

    private final ProductRepository productRepository;
    private final ProductCoverageRepository productCoverageRepository; // YENİ: Buraya ekledik

    @Override
    @Transactional(readOnly = true)
    public List<ProductCoverageRequestDto> getCoveragesByProductId(Long productId) {
        return productCoverageRepository.findByProductId(productId).stream()
                .map(c -> new ProductCoverageRequestDto(productId, c.getCoverageCode(), c.getName(), c.getMinAmount(), c.getMaxAmount()))
                .toList();
    }

    @Override
    @Transactional
    public void addCoverageToProduct(ProductCoverageRequestDto request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı!"));

        ProductCoverageEntity coverage = new ProductCoverageEntity();
        coverage.setProduct(product);
        coverage.setCoverageCode(request.getCoverageCode());
        coverage.setName(request.getName());
        coverage.setMinAmount(request.getMinAmount());
        coverage.setMaxAmount(request.getMaxAmount());
        coverage.setIsActive(true);

        productCoverageRepository.save(coverage);
    }

    @Override
    @Transactional
    public void updateCoverage(Long id, ProductCoverageRequestDto request) {
        ProductCoverageEntity coverage = productCoverageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teminat bulunamadı!"));

        coverage.setCoverageCode(request.getCoverageCode());
        coverage.setName(request.getName());
        coverage.setMinAmount(request.getMinAmount());
        coverage.setMaxAmount(request.getMaxAmount());
        // product_id'yi değiştirmek riskli olabilir, o yüzden sadece kod ve isim güncelliyoruz

        productCoverageRepository.save(coverage);
    }

    @Override
    @Transactional
    public void deleteCoverage(Long id) {
        if (!productCoverageRepository.existsById(id)) {
            throw new RuntimeException("Silinecek teminat bulunamadı!");
        }
        productCoverageRepository.deleteById(id);
    }

}