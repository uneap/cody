package com.cody.domain.store.cache.service;

import static com.cody.domain.store.cache.constants.constants.PRICE_CATEGORY;

import com.cody.domain.store.cache.dto.DisplayProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowHighPriceCategoryService {
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;
    public void refreshLowHighCategoryToAdd(DisplayProduct displayProduct) {
        redisDisplayProductTemplate.opsForZSet().add(getLowHighPriceCategory(displayProduct.getCategoryId()), displayProduct, displayProduct.getProductPrice());
    }

    public void refreshLowHighCategoryToDelete(DisplayProduct displayProduct) {
        redisDisplayProductTemplate.opsForZSet().remove(getLowHighPriceCategory(displayProduct.getCategoryId()), displayProduct);
    }

    public void refreshLowHighCategoryToUpdate(DisplayProduct displayProduct) {
        redisDisplayProductTemplate.opsForZSet().add(getLowHighPriceCategory(displayProduct.getCategoryId()), displayProduct, displayProduct.getProductPrice());
    }
    private String getLowHighPriceCategory(long categoryId) {
        return String.format(PRICE_CATEGORY, categoryId);
    }

}
