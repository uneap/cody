package com.cody.domain.store.category.dto;

import com.cody.common.core.MethodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class CategoryRequestDTO extends CategoryDTO implements Comparable<CategoryRequestDTO> {
    private MethodType methodType;

    @Override
    public int compareTo(CategoryRequestDTO o) {
        return this.getLastModifiedDate().compareTo(o.getLastModifiedDate());
    }

    public static CategoryRequestDTO dtoBuilder(CategoryDTO categoryDTO, MethodType type) {
        return builder()
            .id(categoryDTO.getId())
            .name(categoryDTO.getName())
            .createdDate(categoryDTO.getCreatedDate())
            .lastModifiedDate(categoryDTO.getLastModifiedDate())
            .methodType(type)
            .version(categoryDTO.getVersion()).build();
    }
}
