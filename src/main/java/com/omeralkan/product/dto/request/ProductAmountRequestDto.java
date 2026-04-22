package com.omeralkan.product.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductAmountRequestDto {
    @NotNull(message = "Ürün ID boş olamaz")
    private Long productId;

    @NotNull @Positive(message = "Tutar pozitif olmalıdır")
    private BigDecimal amount;

    @NotNull @FutureOrPresent(message = "Geçerlilik tarihi geçmişte olamaz")
    private LocalDate effectiveDate;
}