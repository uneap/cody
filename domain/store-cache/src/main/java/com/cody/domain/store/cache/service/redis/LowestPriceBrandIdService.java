package com.cody.domain.store.cache.service.redis;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceBrandIdService {
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;
    private static final String LOWEST_PRICE_BRAND_ID_KEY_FORMAT = "lowestPrice:brandId:%d";

    public void refreshProductList(DisplayProduct newProduct, List<DisplayProduct> products) {
        if(!CollectionUtils.isEmpty(products) && isSameCategory(products, newProduct)) {
            products.replaceAll(oldProduct -> isPossibleReplaceByCategory(oldProduct, newProduct) ? newProduct : oldProduct);
        } else if(CollectionUtils.isEmpty(products)){
            products = new ArrayList<>();
            products.add(newProduct);
        } else if(!isSameCategory(products, newProduct)){
            products.add(newProduct);
        }else {
            return;
        }
        redisDisplayProductsTemplate.opsForValue().set(getKey(newProduct.getBrandId()), products);
    }

    public List<DisplayProduct> get(long brandId) {
        String key = getKey(brandId);
        return redisDisplayProductsTemplate.opsForValue().get(key);
    }

    public List<DisplayProduct> updateProduct(DisplayProduct newProduct) {
        List<DisplayProduct> products = get(newProduct.getBrandId());
        if(CollectionUtils.isEmpty(products)) {
            throw new NoSuchElementException("UPDATE OBJECT NOT FOUND");
        }
        products.replaceAll(product -> product.getCategoryId() == newProduct.getCategoryId() ? newProduct : product);
        redisDisplayProductsTemplate.opsForValue().set(getKey(newProduct.getBrandId()), products);
        return products;
    }

    public void removeBrand(long brandId) {
        redisDisplayProductsTemplate.opsForValue().getAndExpire(getKey(brandId), 1L, TimeUnit.MILLISECONDS);
    }

    private boolean isSameCategory(List<DisplayProduct> products, DisplayProduct displayProduct) {
        return products.stream().anyMatch(product -> product.getCategoryId() == displayProduct.getCategoryId());
    }
    private boolean isPossibleReplaceByCategory(DisplayProduct oldProduct, DisplayProduct newProduct) {
        return oldProduct.getCategoryId() == newProduct.getCategoryId()
            && !oldProduct.getLastUpdatedDateTime().isAfter(newProduct.getLastUpdatedDateTime());
    }

    private String getKey(long brandId) {
        return String.format(LOWEST_PRICE_BRAND_ID_KEY_FORMAT, brandId);
    }
}
