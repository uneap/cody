package com.cody.domain.store.brand.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class KafkaBrandDTO extends BrandDTO implements Comparable<KafkaBrandDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(KafkaBrandDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }
}
