package com.omeralkan.product.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductAmountRequestDto {

    private Long productId;
    private BigDecimal amount;
    private LocalDate effectiveDate;
}