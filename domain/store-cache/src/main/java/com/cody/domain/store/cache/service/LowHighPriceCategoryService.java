package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowHighPriceCategoryService {
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;

    public String getLowHighPriceCategory(long categoryId) {
        return String.format("priceCategory:%d", categoryId);
    }
}
