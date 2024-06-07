package com.cody.domain.store.cache.service;


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
class LowestPriceBrandServiceTest {
    @Autowired
    private LowestPriceBrandService lowestPriceBrandService;
    @Test
    void getLowestPriceBrand() {
        Assertions.assertEquals("D",
            lowestPriceBrandService.getLowestPriceBrand().get(0).getBrandName());
    }
}