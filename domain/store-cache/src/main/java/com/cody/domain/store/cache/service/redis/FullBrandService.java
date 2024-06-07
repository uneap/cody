package com.cody.domain.store.cache.service.redis;

import com.cody.domain.store.cache.dto.FullBrand;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class FullBrandService {
    private final RedisTemplate<String, FullBrand> redisBrandTemplate;
    private static final String BRAND_KEY = "brandId:%d";

    private String getKey(long brandId) {
        return String.format(BRAND_KEY, brandId);
    }
    public void add(FullBrand fullBrand) {
        redisBrandTemplate.opsForValue().set(getKey(fullBrand.getId()), fullBrand);
    }

    public void addAll(List<FullBrand> fullBrands) {
        Map<String, FullBrand> brandMap = fullBrands.stream().collect(Collectors.toMap(
            fullBrand -> getKey(fullBrand.getId()), Function.identity()));
        redisBrandTemplate.opsForValue().multiSet(brandMap);
    }

    public FullBrand get(long brandId) {
        return redisBrandTemplate.opsForValue().get(getKey(brandId));
    }
    public void updateAll(List<FullBrand> fullBrands) {
        Map<String, FullBrand> brandMap = fullBrands.stream().collect(Collectors.toMap(
            fullBrand -> getKey(fullBrand.getId()), Function.identity()));
        redisBrandTemplate.opsForValue().multiSet(brandMap);
    }
    public void update(FullBrand fullBrand) {
        redisBrandTemplate.opsForValue().set(getKey(fullBrand.getId()), fullBrand);
    }
    public FullBrand remove(long brandId) {
        return redisBrandTemplate.opsForValue().getAndExpire(getKey(brandId), 1L, TimeUnit.MILLISECONDS);
    }
    public void removeAll(List<Long> brandIds) {
        Set<String> keys = brandIds.stream().map(this::getKey).collect(Collectors.toSet());
        redisBrandTemplate.executePipelined((RedisCallback<FullBrand>) connection -> {
            RedisSerializer keySerializer = redisBrandTemplate.getKeySerializer();
            keys.forEach(brandKey -> {
                byte[] key = keySerializer.serialize(brandKey);
                connection.expire(key, 1);
            });
            return null;
        });
    }

}
