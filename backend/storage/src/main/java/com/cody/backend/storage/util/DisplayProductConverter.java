package com.cody.backend.storage.util;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.cody.domain.store.product.dto.ProductDTO;
import com.cody.domain.store.product.dto.ProductRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class DisplayProductConverter {

    public static List<BrandRequest> convertToBrandRequestDTO(MethodType methodType,
        List<DisplayProduct> displayProducts) {
        if (CollectionUtils.isEmpty(displayProducts)) {
            return new ArrayList<>();
        }
        return displayProducts.stream()
                              .map(brand -> BrandRequest.builder()
                                                        .id(brand.getBrandId())
                                                        .name(brand.getBrandName())
                                                        .methodType(methodType)
                                                        .build())
                              .collect(Collectors.toList());
    }

    public static List<ProductRequestDTO> convertToProductRequestDTO(MethodType methodType,
        List<DisplayProduct> displayProducts) {
        if (CollectionUtils.isEmpty(displayProducts)) {
            return new ArrayList<>();
        }
        return displayProducts.stream()
                              .map(displayProduct -> ProductRequestDTO.builder()
                                                                      .brandId(displayProduct.getBrandId())
                                                                      .categoryId(displayProduct.getCategoryId())
                                                                      .id(displayProduct.getProductId())
                                                                      .price(displayProduct.getProductPrice())
                                                                      .name(displayProduct.getProductName())
                                                                      .methodType(methodType)
                                                                      .build())
                              .collect(Collectors.toList());
    }

    public static List<DisplayProductRequest> convertBrandToDisplayProduct(List<DisplayProduct> requests, List<BrandRequest> queriedBrands, MethodType methodType) {
        if (CollectionUtils.isEmpty(requests) || CollectionUtils.isEmpty(queriedBrands)) {
            return new ArrayList<>();
        }
        Set<Long> brandIds = queriedBrands.stream()
                                          .map(BrandRequest::getId)
                                          .collect(Collectors.toSet());

        return requests.stream()
                       .filter(product -> methodType == MethodType.INSERT || brandIds.contains(product.getBrandId()))
                       .map(displayProduct -> DisplayProductRequest.builder()
                                                                   .methodType(methodType)
                                                                   .productName(displayProduct.getProductName())
                                                                   .productPrice(displayProduct.getProductPrice())
                                                                   .productId(displayProduct.getProductId())
                                                                   .categoryName(displayProduct.getCategoryName())
                                                                   .brandName(displayProduct.getBrandName())
                                                                   .brandId(displayProduct.getBrandId())
                                                                   .lastUpdatedDateTime(displayProduct.getLastUpdatedDateTime())
                                                                   .categoryId(displayProduct.getCategoryId())
                                                                   .build())
                       .collect(Collectors.toList());
    }

    public static List<DisplayProductRequest> convertProductToDisplayProduct(List<DisplayProduct> requests, List<ProductRequestDTO> queriedProducts, MethodType methodType) {
        if (CollectionUtils.isEmpty(requests) || CollectionUtils.isEmpty(queriedProducts)) {
            return new ArrayList<>();
        }
        Set<Long> productIds = queriedProducts.stream()
                                              .map(ProductDTO::getId)
                                              .collect(Collectors.toSet());
        return requests.stream()
                       .filter(product -> methodType == MethodType.INSERT || productIds.contains(product.getProductId()))
                       .map(displayProduct -> DisplayProductRequest.builder()
                                                                   .methodType(methodType)
                                                                   .productName(displayProduct.getProductName())
                                                                   .productPrice(displayProduct.getProductPrice())
                                                                   .productId(displayProduct.getProductId())
                                                                   .categoryName(displayProduct.getCategoryName())
                                                                   .brandName(displayProduct.getBrandName())
                                                                   .brandId(displayProduct.getBrandId())
                                                                   .lastUpdatedDateTime(displayProduct.getLastUpdatedDateTime())
                                                                   .categoryId(displayProduct.getCategoryId())
                                                                   .build())
                       .collect(Collectors.toList());
    }
}
