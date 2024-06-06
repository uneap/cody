package com.cody.domain.store.cache.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;

@Getter
public class DisplayProductRequest extends DisplayProduct {
    private final MethodType methodType;
    private DisplayProduct oldProduct;
    public DisplayProductRequest(MethodType methodType) {
        super();
        this.methodType = methodType;
    }
    public DisplayProductRequest(MethodType methodType, DisplayProduct oldProduct) {
        super();
        this.methodType = methodType;
        this.oldProduct = oldProduct;
    }
}
