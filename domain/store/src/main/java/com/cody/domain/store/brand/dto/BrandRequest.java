package com.cody.domain.store.brand.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class BrandRequest extends BrandDTO implements Comparable<BrandRequest> {
    private MethodType methodType;

    @Override
    public int compareTo(BrandRequest o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static BrandRequest dtoBuilder(BrandDTO brandDTO, MethodType type) {
        return builder()
            .id(brandDTO.getId())
            .name(brandDTO.getName())
            .createdDate(brandDTO.getCreatedDate())
            .lastModifiedDate(brandDTO.getLastModifiedDate())
            .methodType(type)
            .version(brandDTO.getVersion()).build();
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&  methodType != null;
    }
}
