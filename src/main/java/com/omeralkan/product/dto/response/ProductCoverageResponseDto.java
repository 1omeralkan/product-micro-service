package com.omeralkan.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCoverageResponseDto {
    private Long id;
    private String coverageCode;
    private String name;

}