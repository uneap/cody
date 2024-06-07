package com.cody.domain.store.cache.dto;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DisplayProduct {
    private long brandId;
    private long categoryId;
    @JsonInclude(Include.NON_NULL)
    private String brandName;
    private String categoryName;
    private String productName;
    private long productPrice;
    @Setter
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime lastUpdatedDateTime;
    private long productId;
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DisplayProduct product) {
            return this.productId == product.productId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @JsonIgnore
    public boolean isValid() {
        return brandId != 0L
            && categoryName != null
            && categoryId != 0L
            && brandName != null
            && productName != null
            && productPrice != 0L
            && productId != 0L
            && lastUpdatedDateTime != null;
    }

    public DisplayProduct(ZSetProduct zSetProduct, String brandName, long productPrice, String time) {
        this.productId = zSetProduct.getProductId();
        this.brandId = zSetProduct.getBrandId();
        this.categoryId = zSetProduct.getCategoryId();
        this.categoryName = zSetProduct.getCategoryName();
        this.productName = zSetProduct.getProductName();
        this.productPrice = productPrice;
        this.brandName = brandName;
        this.lastUpdatedDateTime = LocalDateTime.parse(time, ISO_LOCAL_DATE_TIME);
    }
}
