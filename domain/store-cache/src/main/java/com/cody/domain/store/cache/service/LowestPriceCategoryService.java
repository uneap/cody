package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowestPriceCategoryService {
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;

    private static final String LOWEST_PRICE_CATEGORY_KEY = "price";
}
