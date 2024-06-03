package com.cody.domain.store.brand.dto;

import com.cody.domain.store.brand.db.BrandDAO;
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
public class BrandDTO {
    private Long id;
    private String name;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static BrandDTO daoBuilder(BrandDAO brandDAO) {
        return builder()
            .id(brandDAO.getId())
            .name(brandDAO.getName())
            .createdDate(brandDAO.getCreatedDate())
            .lastModifiedDate(brandDAO.getLastModifiedDate())
            .version(brandDAO.getVersion()).build();
    }
}
