package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshProductService {
    private final LowestPriceBrandService lowestPriceBrandService;
    private final LowestPriceCategoryService lowestPriceCategoryService;
    private final LowHighPriceCategoryService lowHighPriceCategoryService;

    public void addProductInCache(DisplayProduct displayProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToAdd(displayProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToAdd(displayProduct);
        lowHighPriceCategoryService.refreshLowHighCategoryToAdd(displayProduct);
    }

    public void deleteProductInCache(DisplayProduct displayProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToDelete(displayProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToDelete(displayProduct);
        lowHighPriceCategoryService.refreshLowHighCategoryToDelete(displayProduct);

    }

    public void updateProductInCache(DisplayProduct displayProduct, DisplayProduct oldProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToUpdate(displayProduct, oldProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToUpdate(displayProduct, oldProduct);
        lowHighPriceCategoryService.refreshLowHighCategoryToUpdate(displayProduct, oldProduct);

    }
}
