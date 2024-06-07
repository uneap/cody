package com.cody.domain.store.cache.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor
public class DisplayProductRequest extends DisplayProduct {
    private MethodType methodType;
    @Setter
    private DisplayProduct oldProduct;

    @Override
    public boolean isValid() {
        if(methodType == null) {
            return false;
        }
        if(methodType == MethodType.INSERT) {
            return super.isValid();
        }
        return super.isValid() && oldProduct != null && oldProduct.isValid();
    }
}
