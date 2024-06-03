package com.cody.domain.store.product.dto;

import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.category.dto.CategoryDTO;
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
    private CategoryDTO categoryDTO;
    private BrandDTO brandDTO;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static ProductDTO daoBuilder(ProductDAO productDAO) {
        return builder()
            .id(productDAO.getId())
            .name(productDAO.getName())
            .categoryDTO(CategoryDTO.daoBuilder(productDAO.getCategory()))
            .brandDTO(BrandDTO.daoBuilder(productDAO.getBrand()))
            .createdDate(productDAO.getCreatedDate())
            .lastModifiedDate(productDAO.getLastModifiedDate())
            .version(productDAO.getVersion()).build();
    }
}
