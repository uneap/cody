package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisplayProduct {
    private long brandId;
    private String brandName;
    private long categoryId;
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
