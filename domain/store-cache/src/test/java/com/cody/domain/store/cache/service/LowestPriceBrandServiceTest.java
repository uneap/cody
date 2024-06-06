package com.cody.domain.store.cache.service;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles({"redis-local", "store-cache-local"})
class LowestPriceBrandServiceTest {

    @Autowired
    private LowestPriceBrandService lowestPriceBrandService;

    @Test
    void refreshLowestPriceBrandToAdd() {
        lowestPriceBrandService.refreshLowestPriceBrandToAdd(DisplayProduct.builder()
                                                                           .brandId(1)
                                                                           .productPrice(100)
                                                                           .brandName("A")
                                                                           .productName("217")
                                                                           .productId(217)
                                                                           .lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                                                                           .categoryId(1)
                                                                           .brandId(1)
                                                                           .categoryName("A")
                                                                           .build());
        List<DisplayProduct> products = lowestPriceBrandService.getLowestPriceBrand();
        Assertions.assertEquals(1, products.get(0).getBrandId());
    }
    @Test
    void refreshLowestPriceBrandToUpdate() {
        lowestPriceBrandService.refreshLowestPriceBrandToDelete(DisplayProduct.builder()
                                                                           .brandId(1)
                                                                           .productPrice(20000)
                                                                           .brandName("A")
                                                                           .productName("217")
                                                                           .productId(217)
                                                                           .lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                                                                           .categoryId(1)
                                                                           .brandId(1)
                                                                           .categoryName("A")
                                                                           .build());
        List<DisplayProduct> products = lowestPriceBrandService.getLowestPriceBrand();
        Assertions.assertEquals(4, products.get(0).getBrandId());
    }

    @Test
    void refreshLowestPriceBrandToDelete() {
        lowestPriceBrandService.refreshLowestPriceBrandToAdd(DisplayProduct.builder()
                                                                           .brandId(1)
                                                                           .productPrice(20000)
                                                                           .brandName("A")
                                                                           .productName("217")
                                                                           .productId(217)
                                                                           .lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                                                                           .categoryId(1)
                                                                           .brandId(1)
                                                                           .categoryName("A")
                                                                           .build());
        List<DisplayProduct> products = lowestPriceBrandService.getLowestPriceBrand();
        Assertions.assertEquals(1, products.get(0).getBrandId());
    }
    @Test
    void getLowestPriceBrand() {
        List<DisplayProduct> products = lowestPriceBrandService.getLowestPriceBrand();
        Assertions.assertEquals(4, products.get(0).getBrandId());
    }
}