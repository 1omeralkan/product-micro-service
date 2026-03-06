package com.omeralkan.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String productCode;
    private String name;
    private String description;
    // Kullanıcı ürünü eklerken "Bu ürün şu kategori ID'lerine ait" diyecek
    private Set<Long> categoryIds;
}