package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.service.redis.BrandCategoryFullProductService;
import com.cody.domain.store.cache.service.redis.LowestPriceBrandIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowestPriceCategoryService {
    private final BrandCategoryFullProductService brandCategoryFullProductService;
    private final LowestPriceBrandIdService lowestPriceBrandIdService;

}
