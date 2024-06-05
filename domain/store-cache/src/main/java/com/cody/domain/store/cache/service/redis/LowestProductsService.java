package com.cody.domain.store.cache.service.redis;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestProductsService {
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;
    private static final String LOWEST_PRICE_CATEGORY_KEY = "price";

    public void addByBrand(DisplayProduct newProduct, List<DisplayProduct> products) {
        if(!CollectionUtils.isEmpty(products) && isSameCategory(products, newProduct)) {
            products.replaceAll(oldProduct -> isPossibleReplaceByCategory(oldProduct, newProduct) ? newProduct : oldProduct);
        } else if(CollectionUtils.isEmpty(products)){
            products = new ArrayList<>();
            products.add(newProduct);
        } else if(!isSameCategory(products, newProduct)){
            products.add(newProduct);
        }
        redisDisplayProductsTemplate.opsForValue().set(getLowestPriceBrandKey(newProduct.getBrandId()), products);
    }
    public void addByCategory(DisplayProduct newProduct, List<DisplayProduct> products) {
        if(!CollectionUtils.isEmpty(products)) {
            products.replaceAll(oldProduct -> isPossibleReplaceByBrand(oldProduct, newProduct) ? newProduct : oldProduct);
        } else {
            products = new ArrayList<>();
            products.add(newProduct);
        }
        redisDisplayProductsTemplate.opsForValue().set(LOWEST_PRICE_CATEGORY_KEY, products);
    }
    private boolean isSameCategory(List<DisplayProduct> products, DisplayProduct displayProduct) {
        return products.stream().anyMatch(product -> product.getCategoryId() == displayProduct.getCategoryId());
    }
    private boolean isPossibleReplaceByCategory(DisplayProduct oldProduct, DisplayProduct newProduct) {
        return oldProduct.getCategoryId() == newProduct.getCategoryId()
            && !oldProduct.getLastUpdatedDateTime().isAfter(newProduct.getLastUpdatedDateTime());
    }

    private boolean isPossibleReplaceByBrand(DisplayProduct oldProduct, DisplayProduct newProduct) {
        return oldProduct.getBrandId() == newProduct.getBrandId()
            && !oldProduct.getLastUpdatedDateTime().isAfter(newProduct.getLastUpdatedDateTime());
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
