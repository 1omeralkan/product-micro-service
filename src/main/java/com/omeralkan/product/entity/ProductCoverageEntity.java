package com.omeralkan.product.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "product_coverages", uniqueConstraints = {
        @UniqueConstraint(name = "uq_product_coverage_code", columnNames = {"product_id", "coverage_code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCoverageEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "coverage_code", nullable = false, length = 50)
    private String coverageCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;


}