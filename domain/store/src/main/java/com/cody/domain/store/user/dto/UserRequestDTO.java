package com.cody.domain.store.user.dto;

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

    public static UserRequestDTO dtoBuilder(UserDTO userDTO, MethodType type) {
        return builder()
            .id(userDTO.getId())
            .name(userDTO.getName())
            .createdDate(userDTO.getCreatedDate())
            .lastModifiedDate(userDTO.getLastModifiedDate())
            .methodType(type)
            .version(userDTO.getVersion()).build();
    }
}
