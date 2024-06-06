package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class DisplayProduct {
    private long brandId;
    private long categoryId;
    private String brandName;
    private String categoryName;
    private String productName;
    private long productPrice;
    @Setter
    private LocalDateTime lastUpdatedDateTime;
    private long productId;
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DisplayProduct product) {
            return this.productId == product.productId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
