package com.cody.domain.store.admin.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class AdminRequestDTO extends AdminDTO implements Comparable<AdminRequestDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(AdminRequestDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static AdminRequestDTO dtoBuilder(AdminDTO adminDTO, MethodType type) {
        return builder()
            .userId(adminDTO.getUserId())
            .id(adminDTO.getId())
            .createdDate(adminDTO.getCreatedDate())
            .lastModifiedDate(adminDTO.getLastModifiedDate())
            .methodType(type)
            .version(adminDTO.getVersion()).build();
    }
}
