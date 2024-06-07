package com.cody.backend.storage.util;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.cody.domain.store.product.dto.ProductRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class DisplayProductConverter {

    public static List<BrandRequest> convertToBrandRequestDTO(MethodType methodType, List<BrandDTO> brands) {
        if (CollectionUtils.isEmpty(brands)) {
            return new ArrayList<>();
        }
        return brands.stream()
                              .map(product -> BrandRequest.builder()
                                                        .id(product.getId())
                                                        .name(product.getName())
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

    public static String join(long brandId, long categoryId, String productName) {
        return String.join(",", Long.toString(brandId), Long.toString(categoryId),
            productName);
    }
    public static boolean isSame(DisplayProduct product, MethodType methodType, Map<String, ProductRequestDTO> keyAndProduct){
        return methodType == MethodType.INSERT && keyAndProduct.containsKey(join(product.getBrandId(), product.getCategoryId(),
            product.getProductName()));
    }
    public static List<DisplayProductRequest> convertProductToDisplayProduct(List<DisplayProduct> requests, List<ProductRequestDTO> queriedProducts, MethodType methodType) {
        if (CollectionUtils.isEmpty(requests) || CollectionUtils.isEmpty(queriedProducts)) {
            return new ArrayList<>();
        }
        Map<String, ProductRequestDTO> keyAndProduct = queriedProducts.stream()
                                                          .collect(Collectors.toMap(product -> join(
                                                              product.getBrandId(),
                                                              product.getCategoryId(),
                                                              product.getName()), Function.identity()));
        Set<Long> productIds = queriedProducts.stream().map(ProductRequestDTO::getId).collect(Collectors.toSet());

        return requests.stream()
                       .filter(product ->  isSame(product, methodType, keyAndProduct)  || productIds.contains(product.getProductId()))
                       .map(displayProduct -> {
                           String key = join(displayProduct.getBrandId(), displayProduct.getCategoryId(), displayProduct.getProductName());
                           ProductRequestDTO product = keyAndProduct.get(key);
                           ProductRequestDTO oldProduct = product.getOldProduct();
                            DisplayProductRequest newProduct = DisplayProductRequest.builder()
                                                                   .methodType(methodType)
                                                                   .productName(displayProduct.getProductName())
                                                                   .productPrice(displayProduct.getProductPrice())
                                                                   .productId(displayProduct.getProductId() == 0L ? product.getId() : displayProduct.getProductId())
                                                                   .categoryName(displayProduct.getCategoryName())
                                                                   .brandName(displayProduct.getBrandName())
                                                                   .brandId(displayProduct.getBrandId())
                                                                   .lastUpdatedDateTime(product.getLastModifiedDate())
                                                                   .categoryId(displayProduct.getCategoryId())
                                                                   .build();
                            if(MethodType.UPDATE == methodType) {
                                newProduct.setOldProduct(DisplayProduct.builder()
                                                                       .productName(oldProduct.getName())
                                                                       .productPrice(oldProduct.getPrice())
                                                                       .productId(displayProduct.getProductId())
                                                                       .categoryName(displayProduct.getCategoryName())
                                                                       .brandName(displayProduct.getBrandName())
                                                                       .brandId(displayProduct.getBrandId())
                                                                       .lastUpdatedDateTime(product.getLastModifiedDate())
                                                                       .categoryId(displayProduct.getCategoryId())
                                                                       .build());
                            }
                            return newProduct;
                       }
                       )
                       .collect(Collectors.toList());
    }
}
