package com.cody.domain.store.cache.service.redis;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestProductService {
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;
    private static final String LOWEST_PRICE_CATEGORY_KEY = "price";

    public void addByBrand(DisplayProduct newProduct, List<DisplayProduct> products) {
        String lowestPriceBrandKey = getLowestPriceBrandKey(newProduct.getBrandId());
        add(newProduct, products, lowestPriceBrandKey);
    }
    public void addByCategory(DisplayProduct newProduct, List<DisplayProduct> products) {
        add(newProduct, products, LOWEST_PRICE_CATEGORY_KEY);
    }

    private void add(DisplayProduct newProduct, List<DisplayProduct> products, String key) {
        if(!CollectionUtils.isEmpty(products) && !isSameCategory(products, newProduct)) {
            return;
        }
        if(!CollectionUtils.isEmpty(products)) {
            products.replaceAll(oldProduct -> isPossibleReplace(oldProduct, newProduct) ? newProduct : oldProduct);
        } else {
            products.add(newProduct);
        }
        redisDisplayProductsTemplate.opsForValue().set(key, products);
    }

    private boolean isSameCategory(List<DisplayProduct> products, DisplayProduct displayProduct) {
        return products.stream().anyMatch(product -> product.getCategoryId() == displayProduct.getCategoryId());
    }
    private boolean isPossibleReplace(DisplayProduct oldProduct, DisplayProduct newProduct) {
        return oldProduct.getCategoryId() == newProduct.getCategoryId()
            && oldProduct.getLastUpdatedDateTime().isAfter(newProduct.getLastUpdatedDateTime());
    }

    private List<DisplayProduct> get(String key) {
        return redisDisplayProductsTemplate.opsForValue().get(key);
    }
    private String getLowestPriceBrandKey(long brandId) {
        return String.format("price:brandId:%d", brandId);
    }

    public List<DisplayProduct> getByBrand(long brandId) {
        String lowestPriceBrandKey = getLowestPriceBrandKey(brandId);
        return get(lowestPriceBrandKey);
    }

    public List<DisplayProduct> getByCategory() {
        return get(LOWEST_PRICE_CATEGORY_KEY);
    }
}
