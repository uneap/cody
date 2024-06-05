package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
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
    void refreshLowestPriceBrandToUpdate() {
    }

    @Test
    void refreshLowestPriceBrandToDelete() {
    }

    @Test
    void refreshLowestPriceBrandToAdd() {
        lowestPriceBrandService.refreshLowestPriceBrandToAdd(DisplayProduct.builder().brandId(1)
                                                                           .build());

    }

    @Test
    void getLowestPriceBrand() {
        List<DisplayProduct> products = lowestPriceBrandService.getLowestPriceBrand();
        Assertions.assertEquals(4, products.get(0).getBrandId());
    }
}