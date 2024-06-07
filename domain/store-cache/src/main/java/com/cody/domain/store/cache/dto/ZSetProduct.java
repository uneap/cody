package com.cody.domain.store.cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ZSetProduct {
    private long brandId;
    private long categoryId;
    private String categoryName;
    private String productName;
    private long productPrice;
    @Setter
    private long productId;
    public ZSetProduct(DisplayProduct displayProduct) {
        brandId = displayProduct.getBrandId();
        categoryId = displayProduct.getCategoryId();
        categoryName = displayProduct.getCategoryName();
        productName = displayProduct.getProductName();
        productPrice = displayProduct.getProductPrice();
        productId = displayProduct.getProductId();
    }
}
