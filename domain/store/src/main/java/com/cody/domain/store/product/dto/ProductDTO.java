package com.cody.domain.store.product.dto;

import com.cody.domain.store.product.db.ProductDAO;
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
public class ProductDTO {
    private Long id;
    private String name;
    private Long version;
    private Long categoryId;
    private Long brandId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long price;

    public static ProductDTO daoBuilder(ProductDAO productDAO) {
        return builder()
            .id(productDAO.getId())
            .name(productDAO.getName())
            .categoryId(productDAO.getCategory().getId())
            .brandId(productDAO.getBrand().getId())
            .createdDate(productDAO.getCreatedDate())
            .lastModifiedDate(productDAO.getLastModifiedDate())
            .price(productDAO.getPrice())
            .version(productDAO.getVersion()).build();
    }
}
