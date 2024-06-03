package com.cody.domain.store.seller.dto;

import com.cody.domain.store.seller.db.SellerDAO;
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
    private Long brandId;
    private Long userId;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static UserDTO daoBuilder(SellerDAO sellerDAO) {
        return builder()
            .id(sellerDAO.getId())
            .createdDate(sellerDAO.getCreatedDate())
            .lastModifiedDate(sellerDAO.getLastModifiedDate())
            .brandId(sellerDAO.getBrand().getId())
            .userId(sellerDAO.getUser().getId())
            .version(sellerDAO.getVersion()).build();
    }
}
