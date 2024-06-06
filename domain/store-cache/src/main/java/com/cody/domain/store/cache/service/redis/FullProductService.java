package com.cody.domain.store.cache.service.redis;

import static com.cody.domain.store.cache.constants.constants.PRICE_CATEGORY;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FullProductService {
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;
    public void addByBrandAndCategory(DisplayProduct newProduct) {
        if (newProduct.getProductId() == 0L) {
            return;
        }
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        add(newProduct, lowestPriceBrandZSetKey);
    }
    public void addByCategory(DisplayProduct newProduct) {
        if (newProduct.getProductId() == 0L) {
            return;
        }
        String lowHighPriceCategoryKey = getLowHighPriceCategoryKey(newProduct.getCategoryId());
        add(newProduct, lowHighPriceCategoryKey);
    }
    public void add(DisplayProduct newProduct, String key) {
        String storedTime = redisCommonStringTemplate.opsForValue().get(key);
        if(storedTime != null && LocalDateTime.parse(storedTime, ISO_LOCAL_DATE_TIME).isAfter(newProduct.getLastUpdatedDateTime())) {
            return;
        }
        String updatedTime = newProduct.getLastUpdatedDateTime().format(ISO_LOCAL_DATE_TIME);
        newProduct.setLastUpdatedDateTime(null);
        redisDisplayProductTemplate.opsForZSet().add("price:" + key, newProduct, newProduct.getProductPrice());
        newProduct.setLastUpdatedDateTime(LocalDateTime.parse(updatedTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisCommonStringTemplate.opsForValue().set("time" + key, updatedTime);
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
