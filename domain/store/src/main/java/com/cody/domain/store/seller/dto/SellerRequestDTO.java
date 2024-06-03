package com.cody.domain.store.seller.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class SellerRequestDTO extends SellerDTO implements Comparable<SellerRequestDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(SellerRequestDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static SellerRequestDTO dtoBuilder(SellerDTO sellerDTO, MethodType type) {
        return builder()
            .brandId(sellerDTO.getBrandId())
            .userId(sellerDTO.getUserId())
            .id(sellerDTO.getId())
            .createdDate(sellerDTO.getCreatedDate())
            .lastModifiedDate(sellerDTO.getLastModifiedDate())
            .methodType(type)
            .version(sellerDTO.getVersion()).build();
    }
}
