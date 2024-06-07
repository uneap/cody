package com.cody.domain.store.admin.dto;

import com.cody.domain.store.admin.db.AdminDAO;
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
public class AdminDTO {
    private Long id;
    private Long userId;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static AdminDTO daoBuilder(AdminDAO adminDAO) {
        return builder()
            .id(adminDAO.getId())
            .createdDate(adminDAO.getCreatedDate())
            .lastModifiedDate(adminDAO.getLastModifiedDate())
            .userId(adminDAO.getUser().getId())
            .version(adminDAO.getVersion()).build();
    }
}
