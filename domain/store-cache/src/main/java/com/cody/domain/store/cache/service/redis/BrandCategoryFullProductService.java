package com.cody.domain.store.cache.service.redis;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.FullBrand;
import com.cody.domain.store.cache.dto.ZSetProduct;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandCategoryFullProductService {
    // comment: brandId를 제거할 수 있기에 모든 값을 넣는다.
    private final RedisTemplate<String, String> redisCommonStringTemplate;
    private final RedisTemplate<String, ZSetProduct> redisDisplayProductTemplate;
    private final FullBrandService fullBrandService;
    private static final String BRAND_CATEGORY_KEY_FORMAT = "brandId:%d:categoryId:%d";
    private static final String TIME_BRAND_CATEGORY_KEY_FORMAT = "time:brandCategory:productId:%d";

    public void addProduct(DisplayProduct newProduct) {
        if (newProduct.getProductId() == 0L) {
            return;
        }
        String zSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        String timeKey = getzSetTimeKey(newProduct.getProductId());
        String storedTime = redisCommonStringTemplate.opsForValue().get(timeKey);
        if(storedTime != null && LocalDateTime.parse(storedTime, ISO_LOCAL_DATE_TIME).isAfter(newProduct.getLastUpdatedDateTime())) {
            return;
        }
        redisDisplayProductTemplate.opsForZSet().add(zSetKey, new ZSetProduct(newProduct), newProduct.getProductPrice());
        redisCommonStringTemplate.opsForValue().set(timeKey, newProduct.getLastUpdatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisCommonStringTemplate.opsForValue().getAndExpire(timeKey, 7L, TimeUnit.DAYS);
    }

    public void updateProduct(DisplayProduct newProduct, DisplayProduct oldProduct) {
        if (newProduct.getProductId() == 0L) {
            return;
        }
        String zSetKey = getLowestPriceBrandKey(newProduct.getBrandId(), newProduct.getCategoryId());
        String timeKey = getzSetTimeKey(newProduct.getProductId());
        String storedTime = redisCommonStringTemplate.opsForValue().get(timeKey);
        if(storedTime != null && LocalDateTime.parse(storedTime, ISO_LOCAL_DATE_TIME).isAfter(newProduct.getLastUpdatedDateTime())) {
            return;
        }
        redisDisplayProductTemplate.opsForZSet().remove(zSetKey, new ZSetProduct(oldProduct));
        redisDisplayProductTemplate.opsForZSet().add(zSetKey, new ZSetProduct(newProduct), newProduct.getProductPrice());
        redisCommonStringTemplate.opsForValue().set(timeKey, newProduct.getLastUpdatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        redisCommonStringTemplate.opsForValue().getAndExpire(timeKey, 7L, TimeUnit.DAYS);
    }

    public void removeProduct(DisplayProduct oldProduct) {
        String zSetKey = getLowestPriceBrandKey(oldProduct.getBrandId(), oldProduct.getCategoryId());
        String timeKey = getzSetTimeKey(oldProduct.getProductId());
        redisCommonStringTemplate.opsForValue().getAndExpire(timeKey, 1L, TimeUnit.MILLISECONDS);
        redisDisplayProductTemplate.opsForZSet().remove(zSetKey, new ZSetProduct(oldProduct));
    }

    public void removeBrand(long brandId) {
        //TODO: 추 후 카테고리 변경되면 변경 해야 함.
        redisDisplayProductTemplate.executePipelined((RedisCallback<ZSetProduct>) connection -> {
            RedisSerializer keySerializer = redisCommonStringTemplate.getKeySerializer();
            IntStream.range(1,9).forEach(categoryId -> {
                byte[] key = keySerializer.serialize(getLowestPriceBrandKey(brandId,categoryId));
                connection.expire(key, 1L);
            });
            return null;
        });
    }

    public DisplayProduct getLowestByBrandAndCategory(long brandId, long categoryId) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(brandId, categoryId);
        Set<TypedTuple<ZSetProduct>> productSet = redisDisplayProductTemplate.opsForZSet().rangeWithScores(lowestPriceBrandZSetKey, 0, 0);
        return convertDisplayProduct(productSet);
    }

    public DisplayProduct getHighestByBrandAndCategory(long brandId, long categoryId) {
        String lowestPriceBrandZSetKey = getLowestPriceBrandKey(brandId, categoryId);
        Set<TypedTuple<ZSetProduct>> productSet = redisDisplayProductTemplate.opsForZSet().reverseRangeWithScores(lowestPriceBrandZSetKey, 0, 0);
        return convertDisplayProduct(productSet);
    }

    @Nullable
    private DisplayProduct convertDisplayProduct(Set<TypedTuple<ZSetProduct>> productSet) {
        if(CollectionUtils.isEmpty(productSet)) {
            return null;
        }
        TypedTuple<ZSetProduct> product = productSet.stream().findFirst().get();
        FullBrand brand = fullBrandService.get(product.getValue().getBrandId());
        String time = redisCommonStringTemplate.opsForValue().get(getzSetTimeKey(product.getValue().getProductId()));
        return new DisplayProduct(product.getValue(), brand.getName(), product.getScore().longValue(), time);
    }

    private String getLowestPriceBrandKey(long brandId, long categoryId) {
        return String.format(BRAND_CATEGORY_KEY_FORMAT, brandId, categoryId);
    }
    private String getzSetTimeKey(long productId) {
        return String.format(TIME_BRAND_CATEGORY_KEY_FORMAT, productId);
    }
}
