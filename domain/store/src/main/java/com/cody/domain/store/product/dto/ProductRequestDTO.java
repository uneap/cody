package com.cody.domain.store.product.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDTO extends ProductDTO implements Comparable<ProductRequestDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(ProductRequestDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static ProductRequestDTO dtoBuilder(ProductDTO productDTO, MethodType type) {
        return builder()
            .id(productDTO.getId())
            .name(productDTO.getName())
            .createdDate(productDTO.getCreatedDate())
            .lastModifiedDate(productDTO.getLastModifiedDate())
            .methodType(type)
            .price(productDTO.getPrice())
            .brandId(productDTO.getBrandId())
            .categoryId(productDTO.getCategoryId())
            .version(productDTO.getVersion()).build();
    }
}
