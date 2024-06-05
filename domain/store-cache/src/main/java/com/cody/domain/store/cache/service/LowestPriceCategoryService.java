package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.LowestProductService;
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
    private final LowestProductService lowestProductService;

    public void refreshLowestPriceCategoryToUpdate(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductService.getByCategory();
        fullProductService.addByCategory(product);
        DisplayProduct sameProduct = getSameCategoryProduct(lowestPriceProducts, product.getCategoryId());
        if(sameProduct == null) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product);
    }

    public void refreshLowestPriceCategoryToDelete(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductService.getByCategory();
        fullProductService.removeByCategory(product);
        DisplayProduct sameProduct = getSameCategoryProduct(lowestPriceProducts, product.getCategoryId());
        if(sameProduct == null) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product);
    }

    public void refreshLowestPriceCategoryToAdd(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = lowestProductService.getByCategory();
        fullProductService.addByCategory(product);
        DisplayProduct lowestPriceProductByCategory = getSameCategoryProduct(lowestPriceProducts, product.getCategoryId());
        if (lowestPriceProductByCategory == null || lowestPriceProductByCategory.getProductPrice() > product.getProductPrice()) {
            lowestProductService.addByCategory(product, lowestPriceProducts);
        }
    }

    private void refreshLatestRanking(List<DisplayProduct> lowestPriceProducts, DisplayProduct newProduct) {
        Set<DisplayProduct> lowestPriceProductByCategory = fullProductService.getByCategory(newProduct);
        if(CollectionUtils.isEmpty(lowestPriceProductByCategory)) {
            return;
        }
        lowestProductService.addByCategory(lowestPriceProductByCategory.stream().findFirst().get(), lowestPriceProducts);
    }

    private DisplayProduct getSameCategoryProduct(List<DisplayProduct> products, long categoryId) {
        if (CollectionUtils.isEmpty(products)) {
            return null;
        }
        return products.stream().filter(product -> product.getCategoryId() == categoryId)
                       .findFirst().orElse(null);

    }
}
