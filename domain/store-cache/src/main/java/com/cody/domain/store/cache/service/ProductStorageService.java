package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStorageService {
    private final LowestPriceBrandService lowestPriceBrandService;
    private final LowestPriceCategoryService lowestPriceCategoryService;
    private final LowHighPriceCategoryService lowHighPriceCategoryService;

    public void addProductInCache(DisplayProduct displayProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToAdd(displayProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToAdd(displayProduct);
    }

    public void deleteProductInCache(DisplayProduct displayProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToDelete(displayProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToDelete(displayProduct);
    }

    public void updateProductInCache(DisplayProduct displayProduct) {
        lowestPriceBrandService.refreshLowestPriceBrandToUpdate(displayProduct);
        lowestPriceCategoryService.refreshLowestPriceCategoryToUpdate(displayProduct);
    }
}
