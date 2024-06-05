package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.BrandTotalLowestPriceService;
import com.cody.domain.store.cache.service.redis.FullProductService;
import com.cody.domain.store.cache.service.redis.LowestProductsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceBrandService {
    private final BrandTotalLowestPriceService brandTotalLowestPriceService;
    private final LowestProductsService lowestProductsService;
    private final FullProductService fullProductService;

    public List<DisplayProduct> getLowestPriceBrand() {
        Set<String> lowestPriceBrandId = brandTotalLowestPriceService.get();
        if(CollectionUtils.isEmpty(lowestPriceBrandId)) {
            return new ArrayList<>();
        }
        return lowestProductsService.getByBrand(Long.parseLong(lowestPriceBrandId.stream().findFirst().get()));
    }
    public void refreshLowestPriceBrandToUpdate(DisplayProduct product) {
        fullProductService.addByBrandAndCategory(product);
        List<DisplayProduct> brandRankItems = lowestProductsService.getByBrand(product.getBrandId());
        if (CollectionUtils.isEmpty(brandRankItems)) {
            addLowestPriceBrand(product, new ArrayList<>());
            return;
        }
        DisplayProduct sameLowestPrice = getSameLowestPrice(product, brandRankItems);
        if (sameLowestPrice == null) {
            return;
        }
        Set<DisplayProduct> lowestPriceProductSet = fullProductService.getByBrandAndCategory(product);
        if (!CollectionUtils.isEmpty(lowestPriceProductSet)) {
            DisplayProduct lowestPriceProduct = lowestPriceProductSet.stream().findFirst().orElse(null);
            lowestPriceProduct.setLastUpdatedDateTime(product.getLastUpdatedDateTime());
            refreshLatestRanking(lowestPriceProduct, brandRankItems);
        }
    }

    public void refreshLowestPriceBrandToDelete(DisplayProduct product) {
        fullProductService.removeByBrandAndCategory(product);
        List<DisplayProduct> brandRankItems = lowestProductsService.getByBrand(product.getBrandId());
        if (CollectionUtils.isEmpty(brandRankItems)) {
            return;
        }
        DisplayProduct sameLowestPrice = getSameLowestPrice(product, brandRankItems);
        if (sameLowestPrice == null) {
            return;
        }
        Set<DisplayProduct> lowestPriceProductSet = fullProductService.getByBrandAndCategory(product);
        if(lowestPriceProductSet.isEmpty()) {
            return;
        }
        DisplayProduct lowestPriceProduct = lowestPriceProductSet.stream().findFirst().orElse(null);
        lowestPriceProduct.setLastUpdatedDateTime(product.getLastUpdatedDateTime());
        refreshLatestRanking(lowestPriceProduct, brandRankItems);
    }

    private void refreshLatestRanking(DisplayProduct lowestPriceProduct, List<DisplayProduct> brandRankItems) {
        long totalPrice = brandRankItems.stream().filter(topProduct -> lowestPriceProduct.getProductId() != topProduct.getProductId())
                                  .mapToLong(DisplayProduct::getProductPrice).sum();
        totalPrice = totalPrice + lowestPriceProduct.getProductPrice();
        lowestProductsService.addByBrand(lowestPriceProduct, brandRankItems);
        brandTotalLowestPriceService.add(lowestPriceProduct.getBrandId(), totalPrice, lowestPriceProduct.getLastUpdatedDateTime());
    }

    public void refreshLowestPriceBrandToAdd(DisplayProduct product) {
        List<DisplayProduct> products = lowestProductsService.getByBrand(product.getBrandId());
        if (CollectionUtils.isEmpty(products)) {
            addLowestPriceBrand(product, new ArrayList<>());
            return;
        }
        if(isNewLowest(product, products) || isNewCategory(product, products)) {
            addLowestPriceBrand(product, products);
            return;
        }
        fullProductService.addByBrandAndCategory(product);
    }

    private boolean isNewCategory(DisplayProduct newProduct, List<DisplayProduct> products) {
        return products.stream().noneMatch(product -> product.getCategoryId() == newProduct.getCategoryId());
    }


    private DisplayProduct getSameLowestPrice(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
            .filter(product -> product.getProductId() == addProduct.getProductId())
            .findFirst()
            .orElse(null);
    }

    private boolean isNewLowest(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
                       .filter(product -> product.getCategoryId() == addProduct.getCategoryId())
                       .noneMatch(product -> product.getProductPrice() < addProduct.getProductPrice());
    }
    private void addLowestPriceBrand(DisplayProduct displayProduct, List<DisplayProduct> products) {
        lowestProductsService.addByBrand(displayProduct, products);
        long totalPrice = displayProduct.getProductPrice();
        if(!CollectionUtils.isEmpty(products)) {
            totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        }
        brandTotalLowestPriceService.add(displayProduct.getBrandId(), totalPrice, displayProduct.getLastUpdatedDateTime());
        fullProductService.addByBrandAndCategory(displayProduct);
    }
}
