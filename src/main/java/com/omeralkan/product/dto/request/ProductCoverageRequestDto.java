package com.omeralkan.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCoverageRequestDto {
    private Long productId;
    private String coverageCode;
    private String name;
    private java.math.BigDecimal minAmount;
    private java.math.BigDecimal maxAmount;
}