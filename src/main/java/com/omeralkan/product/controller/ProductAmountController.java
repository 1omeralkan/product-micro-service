package com.omeralkan.product.controller;

import com.omeralkan.product.dto.request.ProductAmountRequestDto;
import com.omeralkan.product.dto.response.ProductAmountResponseDto;
import com.omeralkan.product.service.ProductAmountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-amounts")
@RequiredArgsConstructor
public class ProductAmountController {

    private final ProductAmountService productAmountService;

    @PostMapping
    public ResponseEntity<ProductAmountResponseDto> createProductAmount(
            @Valid @RequestBody ProductAmountRequestDto requestDto) {

        ProductAmountResponseDto response = productAmountService.createProductAmount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAmountResponseDto> getProductAmountById(@PathVariable Long id) {

        ProductAmountResponseDto response = productAmountService.getProductAmountById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductAmountResponseDto>> getAllProductAmounts() {

        List<ProductAmountResponseDto> responses = productAmountService.getAllProductAmounts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAmountResponseDto>> getProductAmountsByProductId(
            @PathVariable Long productId) {

        List<ProductAmountResponseDto> responses = productAmountService.getProductAmountsByProductId(productId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}/active")
    public ResponseEntity<ProductAmountResponseDto> getActiveAmountByProductId(
            @PathVariable Long productId) {

        ProductAmountResponseDto response = productAmountService.getActiveAmountByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAmount(@PathVariable Long id) {

        productAmountService.deleteProductAmount(id);
        return ResponseEntity.noContent().build();
    }
}