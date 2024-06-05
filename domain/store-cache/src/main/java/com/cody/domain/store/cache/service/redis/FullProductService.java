package com.cody.domain.store.cache.service.redis;

import static com.cody.domain.store.cache.constants.constants.PRICE_CATEGORY;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FullProductService {
    // categoryId, brandAndCategoryId 마다 time 체크하는 Redis 추가
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;
    public void addByBrandAndCategory(DisplayProduct newProduct) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        add(newProduct, lowestPriceBrandZSetKey);
    }
    public void addByCategory(DisplayProduct newProduct) {
        String lowHighPriceCategoryKey = getLowHighPriceCategoryKey(newProduct.getCategoryId());
        add(newProduct, lowHighPriceCategoryKey);
    }
    public void add(DisplayProduct newProduct, String key) {
        String storedTime = redisCommonStringTemplate.opsForValue().get(key);
        if(storedTime != null && LocalDateTime.parse(storedTime, ISO_LOCAL_DATE_TIME).isAfter(newProduct.getLastUpdatedDateTime())) {
            return;
        }
        redisDisplayProductTemplate.opsForZSet().add("price:" + key, newProduct, newProduct.getProductPrice());
        redisCommonStringTemplate.opsForValue().set("time" + key, newProduct.getLastUpdatedDateTime().format(ISO_LOCAL_DATE_TIME));
    }
    public void removeByBrandAndCategory(DisplayProduct newProduct) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        remove(newProduct, lowestPriceBrandZSetKey);
    }

    public void removeByCategory(DisplayProduct newProduct) {
        String lowHighPriceCategoryKey = getLowHighPriceCategoryKey(newProduct.getCategoryId());
        remove(newProduct, lowHighPriceCategoryKey);
    }
    public void remove(DisplayProduct newProduct, String key) {
        redisDisplayProductTemplate.opsForZSet().remove(key, newProduct);
    }

    public Set<DisplayProduct> getByBrandAndCategory(DisplayProduct newProduct) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        return get(lowestPriceBrandZSetKey);
    }

    public Set<DisplayProduct> getByCategory(long categoryId) {
        String lowHighPriceCategoryKey = getLowHighPriceCategoryKey(categoryId);
        return get(lowHighPriceCategoryKey);
    }
    public Set<DisplayProduct> get(String key) {
        return redisDisplayProductTemplate.opsForZSet().range(key, 0, 0);
    }
    private String getLowestPriceBrandKey(long brandId, long categoryId) {
        return String.format("brandId:%d:categoryId:%d", brandId, categoryId);
    }
    private String getLowHighPriceCategoryKey(long categoryId) {
        return String.format(PRICE_CATEGORY, categoryId);
    }
}
