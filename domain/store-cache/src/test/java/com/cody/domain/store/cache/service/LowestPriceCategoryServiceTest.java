package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.Arrays;
import java.util.List;
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
class LowestPriceCategoryServiceTest {
    @Autowired
    private LowestPriceCategoryService lowestPriceCategoryService;
    @Test
    void getLowestPriceCategoryService() {
        // category 스니커즈의 경우, G와 A와 동일함.
        List<String> brandNames= Arrays.asList("C","E","D","G","A","D","I","F");
        List<DisplayProduct> products = lowestPriceCategoryService.getLowestPriceCategories();
        Assertions.assertTrue(() -> {
            for(int i = 0; i < products.size(); i++) {
                if(!brandNames.get(i).equals(products.get(i).getBrandName())) {
                    return false;
                }
            }
            return true;
        });

    }
}