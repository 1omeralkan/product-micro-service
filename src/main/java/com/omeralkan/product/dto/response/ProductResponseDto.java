package com.omeralkan.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Boolean isActive;
    private BigDecimal amount;
    private Set<CategoryResponseDto> categories;
    private List<ProductCoverageResponseDto> coverages;
}