package com.cody.domain.store.cache.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;

@Getter
public class DisplayProductRequest extends DisplayProduct {
    private final MethodType methodType;

    public DisplayProductRequest(MethodType methodType) {
        super();
        this.methodType = methodType;
    }
}
