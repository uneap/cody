package com.cody.domain.store.user.dto;

import com.cody.domain.store.user.db.UserDAO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static UserDTO daoBuilder(UserDAO userDAO) {
        return builder()
            .id(userDAO.getId())
            .name(userDAO.getName())
            .createdDate(userDAO.getCreatedDate())
            .lastModifiedDate(userDAO.getLastModifiedDate())
            .version(userDAO.getVersion()).build();
    }
}
