package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceBrandService {
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private static final String LOWEST_PRICE_BRAND_ZSET_KEY = "priceBrand";
    public void refreshLowestPriceBrandToUpdate(DisplayProduct product) {
        addElementToBrandCategoryRanks(product);
        List<DisplayProduct> brandRankItems = getLowestPriceBrandProducts(product);
        if (CollectionUtils.isEmpty(brandRankItems)) {
            addLowestPriceBrand(product, new ArrayList<>());
            return;
        }
        DisplayProduct sameLowestPrice = getSameLowestPrice(product, brandRankItems);
        if (sameLowestPrice == null) {
            return;
        }
        Set<DisplayProduct> lowestPriceProductSet = getLowestPriceByCategory(product);
        if (!CollectionUtils.isEmpty(lowestPriceProductSet)) {
            DisplayProduct lowestPriceProduct = lowestPriceProductSet.stream().findFirst().orElse(null);
            refreshLatestRanking(lowestPriceProduct, brandRankItems, product.getBrandId());

        }
    }

    public void refreshLowestPriceBrandToDelete(DisplayProduct product) {
        removeElementToBrandCategoryRanks(product);
        List<DisplayProduct> brandRankItems = getLowestPriceBrandProducts(product);
        if (CollectionUtils.isEmpty(brandRankItems)) {
            return;
        }
        DisplayProduct sameLowestPrice = getSameLowestPrice(product, brandRankItems);
        if (sameLowestPrice == null) {
            return;
        }
        Set<DisplayProduct> lowestPriceProductSet = getLowestPriceByCategory(product);
        if(lowestPriceProductSet.isEmpty()) {
            return;
        }
        DisplayProduct lowestPriceProduct = lowestPriceProductSet.stream().findFirst().orElse(null);
        refreshLatestRanking(lowestPriceProduct, brandRankItems, product.getBrandId());
    }

    private void refreshLatestRanking(DisplayProduct lowestPriceProduct, List<DisplayProduct> brandRankItems, long brandId) {
        long totalPrice = brandRankItems.stream().filter(topProduct -> lowestPriceProduct.getProductId() != topProduct.getProductId())
                                  .mapToLong(DisplayProduct::getProductPrice).sum();
        totalPrice = totalPrice + lowestPriceProduct.getProductPrice();
        addElementToBrandRanks(brandId, totalPrice);
        addElementToBrandProducts(lowestPriceProduct, brandRankItems);
    }

    public void refreshLowestPriceBrandToAdd(DisplayProduct product) {
        List<DisplayProduct> products = getLowestPriceBrandProducts(product);
        if (CollectionUtils.isEmpty(products)) {
            addLowestPriceBrand(product, new ArrayList<>());
            return;
        }
        DisplayProduct lowestPriceBrandByCategory = getLowestPriceBrandByCategory(product, products);
        if(lowestPriceBrandByCategory == null) {
            addElementToBrandCategoryRanks(product);
            return;
        }
        addLowestPriceBrand(product, products);
    }

    private Set<DisplayProduct> getLowestPriceByCategory(DisplayProduct displayProduct) {
        return redisDisplayProductTemplate.opsForZSet().range(getLowestPriceBrandZSetKey(
            displayProduct.getBrandId(), displayProduct.getCategoryId()), 0, 0);
    }

    private DisplayProduct getSameLowestPrice(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
            .filter(product -> product.getProductId() == addProduct.getProductId())
            .findFirst()
            .orElse(null);
    }

    private List<DisplayProduct> getLowestPriceBrandProducts(DisplayProduct product) {
        long brandId = product.getBrandId();
        String lowestPriceBrandKey = getLowestPriceBrandKey(brandId);
        return redisDisplayProductsTemplate.opsForValue().get(lowestPriceBrandKey);
    }

    private String getLowestPriceBrandZSetKey(long brandId, long categoryId) {
        return String.format("price:brandId:%d:categoryId:%d", brandId, categoryId);
    }

    private String getLowestPriceBrandKey(long brandId) {
        return String.format("price:brandId:%d", brandId);
    }

    private DisplayProduct getLowestPriceBrandByCategory(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
                       .filter(product -> product.getCategoryId() == addProduct.getCategoryId())
                       .filter(product -> product.getProductPrice() > addProduct.getProductPrice())
                       .findFirst()
                       .orElse(null);
    }
    private void addLowestPriceBrand(DisplayProduct displayProduct, List<DisplayProduct> products) {
        addElementToBrandProducts(displayProduct, products);
        long totalPrice = 0L;
        if(!CollectionUtils.isEmpty(products)) {
            totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        }
        addElementToBrandRanks(displayProduct.getBrandId(), totalPrice);
        addElementToBrandCategoryRanks(displayProduct);
    }

    private void addElementToBrandProducts(DisplayProduct displayProduct, List<DisplayProduct> products) {
        String lowestPriceBrandKey = getLowestPriceBrandKey(displayProduct.getBrandId());
        if(!CollectionUtils.isEmpty(products)) {
            products = products.stream()
                               .filter(product -> displayProduct.getCategoryId() != product.getCategoryId())
                               .collect(Collectors.toList());
        }
        products.add(displayProduct);
        redisDisplayProductsTemplate.opsForValue().set(lowestPriceBrandKey, products);
    }
    private void addElementToBrandRanks(long brandId, long totalPrice) {
        redisCommonStringTemplate.opsForZSet().add(LOWEST_PRICE_BRAND_ZSET_KEY, String.valueOf(brandId), totalPrice);
    }

    private void addElementToBrandCategoryRanks(DisplayProduct displayProduct) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandZSetKey(displayProduct.getBrandId(), displayProduct.getCategoryId());
        redisDisplayProductTemplate.opsForZSet().add(lowestPriceBrandZSetKey, displayProduct, displayProduct.getProductPrice());
    }
    private void removeElementToBrandCategoryRanks(DisplayProduct displayProduct) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandZSetKey(displayProduct.getBrandId(), displayProduct.getCategoryId());
        redisDisplayProductTemplate.opsForZSet().remove(lowestPriceBrandZSetKey, displayProduct);
    }

}
