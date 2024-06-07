package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.LowHighProduct;
import com.cody.domain.store.cache.dto.PriceLevel;
import com.cody.domain.store.cache.service.redis.BrandCategoryFullProductService;
import com.cody.domain.store.cache.service.redis.PriceCategoryService;
import io.micrometer.common.lang.Nullable;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowHighPriceCategoryService {
    private final BrandCategoryFullProductService brandCategoryFullProductService;
    private final PriceCategoryService priceCategoryService;

    public LowHighProduct getLowHighProduct(long categoryId) {
        Set<TypedTuple<String>> lowestBrand = priceCategoryService.getBrandIdAndPrice(categoryId, PriceLevel.LOWEST);
        Set<TypedTuple<String>> highestBrand = priceCategoryService.getBrandIdAndPrice(categoryId, PriceLevel.HIGHEST);
        return LowHighProduct.builder()
                             .lowestProduct(convertDisplayProduct(categoryId, lowestBrand, PriceLevel.LOWEST))
                             .highestProduct(convertDisplayProduct(categoryId, highestBrand, PriceLevel.HIGHEST))
                             .build();
    }

    @Nullable
    private DisplayProduct convertDisplayProduct(long categoryId, Set<TypedTuple<String>> product, PriceLevel priceLevel) {
        if (CollectionUtils.isEmpty(product)) {
            return null;
        }
        TypedTuple<String> brandIdAndScore = product.stream().findFirst().get();
        long brandId = Long.parseLong(brandIdAndScore.getValue());
        if (PriceLevel.LOWEST == priceLevel) {
            return brandCategoryFullProductService.getLowestByBrandAndCategory(brandId, categoryId);

        } else {
            return brandCategoryFullProductService.getHighestByBrandAndCategory(brandId, categoryId);
        }
    }


}
