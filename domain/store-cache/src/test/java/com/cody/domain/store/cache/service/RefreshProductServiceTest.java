package com.cody.domain.store.cache.service;


import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.BrandCategoryFullProductService;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles({"store-cache-local", "redis-local"})
class RefreshProductServiceTest {
    @Autowired
    private RefreshProductService refreshProductService;
    @Autowired
    private BrandCategoryFullProductService brandCategoryFullProductService;
    @Autowired
    private LowestPriceCategoryService lowestPriceCategoryService;
    @Test
    void addProductInCache_2() {
        DisplayProduct product = DisplayProduct.builder()
                                               .productId(217)
                                               .brandId(17)
                                               .categoryId(1)
                                               .productPrice(10)
                                               .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build();
        refreshProductService.addProductInCache(product);
        Assertions.assertEquals(product, brandCategoryFullProductService.getLowestByBrandAndCategory(9,1));
    }
    @Test
    void deleteBrandInCache() {
        refreshProductService.deleteBrandInCache(1);
        Assertions.assertFalse(lowestPriceCategoryService.getLowestPriceCategories()
                                                         .stream()
                                                         .anyMatch(product -> product.getBrandName().equals("A")));
    }

    @Test
    void addProductInCache() {
        DisplayProduct product = DisplayProduct.builder()
                                               .productId(217)
                                               .brandId(9)
                                               .categoryId(1)
                                               .productPrice(10)
                                               .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build();
        refreshProductService.addProductInCache(product);
        Assertions.assertEquals(product, brandCategoryFullProductService.getLowestByBrandAndCategory(9,1));
    }


    @Test
    void updateProductInCache() {
        DisplayProduct oldProduct = DisplayProduct.builder()
                                               .productId(217)
                                               .brandId(9)
                                               .categoryId(1)
                                               .productPrice(10)
                                               .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build();
        DisplayProduct product = DisplayProduct.builder()
                                               .productId(217)
                                               .brandId(9)
                                               .categoryId(1)
                                               .productPrice(1500000)
                                               .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build();
        refreshProductService.updateProductInCache(product, oldProduct);
        Assertions.assertEquals(product, brandCategoryFullProductService.getHighestByBrandAndCategory(9,1));
        Assertions.assertNotEquals(product, brandCategoryFullProductService.getLowestByBrandAndCategory(9, 1));
    }

    @Test
    void deleteProductInCache() {
        DisplayProduct product = DisplayProduct.builder()
                                               .productId(217)
                                               .brandId(9)
                                               .categoryId(1)
                                               .productPrice(1500000)
                                               .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build();
        refreshProductService.deleteProductInCache(DisplayProduct.builder()
                                                              .productId(217)
                                                              .brandId(9)
                                                              .categoryId(1)
                                                              .productPrice(1500000)
                                                              .brandName("I").lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))).build());
        Assertions.assertNotEquals(product, brandCategoryFullProductService.getHighestByBrandAndCategory(9,1));

    }
}