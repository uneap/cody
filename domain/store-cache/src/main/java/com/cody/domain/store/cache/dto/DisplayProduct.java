package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class DisplayProduct {
    private final long brandId;
    private final String brandName;
    private final long categoryId;
    private final String categoryName;
    private final String productName;
    private final long productPrice;
    @Setter
    private LocalDateTime lastUpdatedDateTime;
    private final long productId;
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
