package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.BrandTotalLowestPriceService;
import com.cody.domain.store.cache.service.redis.FullProductService;
import com.cody.domain.store.cache.service.redis.LowestProductService;
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
    private final LowestProductService lowestProductService;
    private final FullProductService fullProductService;

    public List<DisplayProduct> getLowestPriceBrand() {
        Set<String> lowestPriceBrandId = brandTotalLowestPriceService.get();
        if(CollectionUtils.isEmpty(lowestPriceBrandId)) {
            return new ArrayList<>();
        }
        return lowestProductService.getByBrand(Long.parseLong(lowestPriceBrandId.stream().findFirst().get()));
    }
    public void refreshLowestPriceBrandToUpdate(DisplayProduct product) {
        fullProductService.addByBrandAndCategory(product);
        List<DisplayProduct> brandRankItems = lowestProductService.getByBrand(product.getBrandId());
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
        List<DisplayProduct> brandRankItems = lowestProductService.getByBrand(product.getBrandId());
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
        lowestProductService.addByBrand(lowestPriceProduct, brandRankItems);
        brandTotalLowestPriceService.add(lowestPriceProduct.getBrandId(), totalPrice, lowestPriceProduct.getLastUpdatedDateTime());
    }

    public void refreshLowestPriceBrandToAdd(DisplayProduct product) {
        List<DisplayProduct> products = lowestProductService.getByBrand(product.getBrandId());
        if (CollectionUtils.isEmpty(products)) {
            addLowestPriceBrand(product, new ArrayList<>());
            return;
        }
        DisplayProduct lowestPriceBrandByCategory = getLowestPriceBrandByCategory(product, products);
        if(lowestPriceBrandByCategory == null) {
            fullProductService.addByBrandAndCategory(product);
            return;
        }
        addLowestPriceBrand(product, products);
    }

    private DisplayProduct getSameLowestPrice(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
            .filter(product -> product.getProductId() == addProduct.getProductId())
            .findFirst()
            .orElse(null);
    }

    private DisplayProduct getLowestPriceBrandByCategory(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
                       .filter(product -> product.getCategoryId() == addProduct.getCategoryId())
                       .filter(product -> product.getProductPrice() > addProduct.getProductPrice())
                       .findFirst()
                       .orElse(null);
    }
    private void addLowestPriceBrand(DisplayProduct displayProduct, List<DisplayProduct> products) {
        lowestProductService.addByBrand(displayProduct, products);
        long totalPrice = 0L;
        if(!CollectionUtils.isEmpty(products)) {
            totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        }
        brandTotalLowestPriceService.add(displayProduct.getBrandId(), totalPrice, displayProduct.getLastUpdatedDateTime());
        fullProductService.addByBrandAndCategory(displayProduct);
    }
}
