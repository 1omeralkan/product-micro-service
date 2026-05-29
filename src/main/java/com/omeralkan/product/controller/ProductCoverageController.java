package com.omeralkan.product.controller;

import com.omeralkan.product.dto.request.ProductCoverageRequestDto;
import com.omeralkan.product.service.ProductCoverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-coverages")
@RequiredArgsConstructor
public class ProductCoverageController {

    private final ProductCoverageService productCoverageService;

    @PostMapping
    public ResponseEntity<Void> addCoverageToProduct(@RequestBody ProductCoverageRequestDto request) {
        productCoverageService.addCoverageToProduct(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductCoverageRequestDto>> getCoverages(@PathVariable Long productId) {
        return ResponseEntity.ok(productCoverageService.getCoveragesByProductId(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCoverage(@PathVariable Long id, @RequestBody ProductCoverageRequestDto request) {
        productCoverageService.updateCoverage(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoverage(@PathVariable Long id) {
        productCoverageService.deleteCoverage(id);
        return ResponseEntity.noContent().build();
    }
}