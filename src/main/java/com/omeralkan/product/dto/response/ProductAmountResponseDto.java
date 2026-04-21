package com.omeralkan.product.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductAmountResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal amount;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private Boolean isActive;
}