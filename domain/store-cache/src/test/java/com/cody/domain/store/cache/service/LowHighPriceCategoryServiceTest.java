package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.LowHighProduct;
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
class LowHighPriceCategoryServiceTest {
    @Autowired
    private LowHighPriceCategoryService lowHighPriceCategoryService;
    @Test
    void getLowHighProduct() {
        LowHighProduct lowHighProduct = lowHighPriceCategoryService.getLowHighProduct(1);
        Assertions.assertEquals("C", lowHighProduct.getLowestProduct().getBrandName());
        Assertions.assertEquals("I", lowHighProduct.getHighestProduct().getBrandName());
    }
}