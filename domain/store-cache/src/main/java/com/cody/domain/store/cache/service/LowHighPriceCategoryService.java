package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.FullProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowHighPriceCategoryService {
    private final FullProductService fullProductService;
    public void refreshLowHighCategoryToAdd(DisplayProduct displayProduct) {
        fullProductService.addByCategory(displayProduct);
    }

    public void refreshLowHighCategoryToDelete(DisplayProduct displayProduct) {
        fullProductService.removeByCategory(displayProduct);
    }

    public void refreshLowHighCategoryToUpdate(DisplayProduct displayProduct) {
        fullProductService.addByCategory(displayProduct);
    }


}
