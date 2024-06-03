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
public class UserRequestDTO extends UserDTO implements Comparable<UserRequestDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(UserRequestDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static UserRequestDTO dtoBuilder(UserDTO sellerDTO, MethodType type) {
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
