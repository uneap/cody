package com.cody.domain.brand;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class BrandDTO {
    private Long id;
    @Setter
    private String name;
    @Setter
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
