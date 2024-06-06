package com.cody.domain.store.product.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UniqueProduct {
    private final long brandId;
    private final String name;
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UniqueProduct product) {
            return this.brandId == product.brandId && this.name.equals(product.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, name.hashCode());
    }
}
