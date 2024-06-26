package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.LowestFullCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowestPriceCategoryService {
    private final LowestFullCategoryService lowestFullCategoryService;

    public List<DisplayProduct> getLowestPriceCategories() {
        return lowestFullCategoryService.get();
    }
}
