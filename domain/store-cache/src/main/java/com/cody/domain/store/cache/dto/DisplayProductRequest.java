package com.cody.domain.store.cache.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor
public class DisplayProductRequest extends DisplayProduct {
    private MethodType methodType;
    private DisplayProduct oldProduct;
    public DisplayProductRequest(MethodType methodType) {
        super();
        this.methodType = methodType;
    }
    @Override
    public boolean isValid() {
        return super.isValid() && oldProduct != null && oldProduct.isValid() && methodType != null;
    }
    public DisplayProductRequest(MethodType methodType, DisplayProduct oldProduct) {
        super();
        this.methodType = methodType;
        this.oldProduct = oldProduct;
    }
}
