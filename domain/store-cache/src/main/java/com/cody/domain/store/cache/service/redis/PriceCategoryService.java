package com.cody.domain.store.cache.service.redis;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.PriceLevel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceCategoryService {
    // comment: brandId를 제거하기 어렵기에 모든 값을 다 넣지 않고, 브랜드 중 가장 큰/작은 값만 넣는다.
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private static final String LOWEST_PRICE_CATEGORY_ID_KEY_FORMAT = "LowestPrice:category:%d";
    private static final String HIGHEST_PRICE_CATEGORY_ID_KEY_FORMAT = "HighestPrice:category:%d";
    private static final String LOWEST_PRICE_TIME_KEY_FORMAT = "LowestPrice:product:%d";
    private static final String HIGHEST_PRICE_TIME_KEY_FORMAT = "HighestPrice:product:%d";

    public Set<TypedTuple<String>> getBrandIdAndPrice(long categoryId, PriceLevel priceLevel) {
        return priceLevel == PriceLevel.LOWEST ? redisCommonStringTemplate.opsForZSet().rangeWithScores(getProductKey(priceLevel, categoryId), 0, 0)
            : redisCommonStringTemplate.opsForZSet().reverseRangeWithScores(getProductKey(priceLevel, categoryId), 0, 0);
    }

    public void refreshProduct(DisplayProduct newProduct, PriceLevel priceLevel) {
        String timeKey = getTimeKey(priceLevel, newProduct.getProductId());
        String storedTime = redisCommonStringTemplate.opsForValue().get(timeKey);
        if (storedTime != null && LocalDateTime.parse(storedTime, ISO_LOCAL_DATE_TIME).isAfter(newProduct.getLastUpdatedDateTime())) {
            return;
        }
        redisCommonStringTemplate.opsForZSet().add(getProductKey(priceLevel, newProduct.getCategoryId()), String.valueOf(newProduct.getBrandId()), newProduct.getProductPrice());
        redisCommonStringTemplate.opsForValue().set(timeKey, newProduct.getLastUpdatedDateTime().format(ISO_LOCAL_DATE_TIME));
        redisCommonStringTemplate.opsForValue().getAndExpire(timeKey, 7L, TimeUnit.DAYS);
    }

    public void removeBrand(long brandId) {
        //TODO: 추 후 카테고리 변경되면 변경 해야 함.
        redisCommonStringTemplate.executePipelined((RedisCallback<AllUser>) connection -> {
            RedisSerializer keySerializer = redisCommonStringTemplate.getKeySerializer();
            RedisSerializer valueSerializer = redisCommonStringTemplate.getValueSerializer();
            IntStream.range(1,9).forEach(categoryId -> {
                byte[] highKey = keySerializer.serialize(getProductKey(PriceLevel.HIGHEST,categoryId));
                byte[] lowKey = keySerializer.serialize(getProductKey(PriceLevel.LOWEST,categoryId));
                byte[] mem = valueSerializer.serialize(Long.toString(brandId));
                connection.zRem(highKey, mem);
                connection.zRem(lowKey, mem);
            });
            return null;
        });
    }

    private String getProductKey(PriceLevel priceLevel, long categoryId) {
        return priceLevel == PriceLevel.LOWEST ? String.format(LOWEST_PRICE_CATEGORY_ID_KEY_FORMAT, categoryId)
            : String.format(HIGHEST_PRICE_CATEGORY_ID_KEY_FORMAT, categoryId);
    }

    private String getTimeKey(PriceLevel priceLevel, long productId) {
        return priceLevel == PriceLevel.LOWEST ? String.format(LOWEST_PRICE_TIME_KEY_FORMAT, productId)
            : String.format(HIGHEST_PRICE_TIME_KEY_FORMAT, productId);
    }
}
