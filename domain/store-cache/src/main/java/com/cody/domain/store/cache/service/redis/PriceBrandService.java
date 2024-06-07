package com.cody.domain.store.cache.service.redis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceBrandService {
    //BrandId마다 time 체크 하는 redis 추가
    //localDateTime 체크 완료
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private static final String LOWEST_PRICE_BRAND_ZSET_KEY = "priceBrand";

    public String getBrandIdKey(long brandId) {
        return String.format("BrandId:%d", brandId);
    }

    public void refreshRank(long brandId, long totalPrice, LocalDateTime updateTime) {
        String timeKey = getBrandIdKey(brandId);
        String storedTime = redisCommonStringTemplate.opsForValue().get(timeKey);
        if(storedTime != null && LocalDateTime.parse(storedTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(updateTime)) {
            return;
        }
        redisCommonStringTemplate.opsForZSet().add(LOWEST_PRICE_BRAND_ZSET_KEY, Long.toString(brandId), totalPrice);
        redisCommonStringTemplate.opsForValue().set(timeKey, updateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisCommonStringTemplate.opsForValue().getAndExpire(timeKey, 7L, TimeUnit.DAYS);
    }
    public void removeBrand(long brandId) {
        redisCommonStringTemplate.opsForZSet().remove(LOWEST_PRICE_BRAND_ZSET_KEY, Long.toString(brandId));
    }

    public Set<String> get() {
        return redisCommonStringTemplate.opsForZSet().range(LOWEST_PRICE_BRAND_ZSET_KEY, 0, 0);
    }
}
