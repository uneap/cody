package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.redis.LowestPriceBrandIdService;
import com.cody.domain.store.cache.service.redis.PriceBrandService;
import com.cody.domain.store.cache.service.redis.BrandCategoryFullProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceBrandService {
    private final PriceBrandService priceBrandService;
    private final LowestPriceBrandIdService lowestPriceBrandIdService;
    private final BrandCategoryFullProductService brandCategoryFullProductService;

    public List<DisplayProduct> getLowestPriceBrand() {
        Set<String> lowestPriceBrandId = priceBrandService.get();
        if(CollectionUtils.isEmpty(lowestPriceBrandId)) {
            return new ArrayList<>();
        }
        return lowestPriceBrandIdService.get(Long.parseLong(lowestPriceBrandId.stream().findFirst().get()));
    }
}
