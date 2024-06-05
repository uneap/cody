package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.LowestProductsService;
import com.cody.domain.store.cache.service.redis.FullProductService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceCategoryService {
    private final FullProductService fullProductService;
    private final LowestProductsService lowestProductsService;

    public void refreshLowestPriceCategoryToUpdate(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductsService.getByCategory();
        fullProductService.addByCategory(product);
        if(isSameCategory(lowestPriceProducts, product.getCategoryId())) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product);
    }

    public void refreshLowestPriceCategoryToDelete(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductsService.getByCategory();
        fullProductService.removeByCategory(product);
        if(isSameCategory(lowestPriceProducts, product.getCategoryId())) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product);
    }

    public void refreshLowestPriceCategoryToAdd(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductsService.getByCategory();
        fullProductService.addByCategory(product);
        DisplayProduct lowestPriceProductByCategory = getLowestProductByCategory(lowestPriceProducts, product.getCategoryId());
        if (lowestPriceProductByCategory == null || lowestPriceProductByCategory.getProductPrice() > product.getProductPrice()) {
            lowestProductsService.addByCategory(product, lowestPriceProducts);
        }
    }

    private void refreshLatestRanking(List<DisplayProduct> lowestPriceProducts, DisplayProduct newProduct) {
        Set<DisplayProduct> lowestPriceProductsByCategory = fullProductService.getByCategory(newProduct.getCategoryId());
        if(CollectionUtils.isEmpty(lowestPriceProductsByCategory)) {
            return;
        }
        DisplayProduct lowestPriceProductByCategory = getLowestProductByCategory(lowestPriceProducts, newProduct.getCategoryId());
        if (lowestPriceProductByCategory == null || lowestPriceProductByCategory.getProductPrice() > newProduct.getProductPrice()) {
            lowestProductsService.addByCategory(newProduct, lowestPriceProducts);
        }
    }

    private boolean isSameCategory(List<DisplayProduct> products, long categoryId) {
        if (CollectionUtils.isEmpty(products)) {
            return false;
        }
        return products.stream().anyMatch(product -> product.getCategoryId() == categoryId);
    }

    private DisplayProduct getLowestProductByCategory(List<DisplayProduct> products, long categoryId) {
        if (CollectionUtils.isEmpty(products)) {
            return null;
        }
        return products.stream()
                       .filter(product -> product.getCategoryId() == categoryId)
                       .findFirst()
                       .orElse(null);
    }
}
