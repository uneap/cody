package com.cody.domain.store.cache.service.redis;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestFullCategoryService {
    private static final String LOWEST_FULL_CATEGORY_KEY = "LowestFullCategory:";
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;

    public void addProductList(DisplayProduct newProduct) {
        List<DisplayProduct> lowestProductsByCategory = get();
        setLowestProducts(newProduct, lowestProductsByCategory);
    }

    public void update(DisplayProduct oldProduct, DisplayProduct newProduct) {
        List<DisplayProduct> lowestProductsByCategory = get();
        lowestProductsByCategory.removeIf(product -> oldProduct.getProductId() == product.getProductId());
        setLowestProducts(newProduct, lowestProductsByCategory);
    }

    private void setLowestProducts(DisplayProduct newProduct, List<DisplayProduct> lowestProductsByCategory) {
        if(!CollectionUtils.isEmpty(lowestProductsByCategory) && isSameCategory(lowestProductsByCategory, newProduct)) {
            lowestProductsByCategory.replaceAll(product -> isChange(newProduct, product) ? newProduct : product);
        } else if(CollectionUtils.isEmpty(lowestProductsByCategory)) {
            lowestProductsByCategory = new ArrayList<>();
            lowestProductsByCategory.add(newProduct);
        }  else if (!isSameCategory(lowestProductsByCategory, newProduct)) {
            lowestProductsByCategory.add(newProduct);
        } else {
            return;
        }
        redisDisplayProductsTemplate.opsForValue().set(LOWEST_FULL_CATEGORY_KEY, lowestProductsByCategory);
    }

    public List<DisplayProduct> get() {
        return redisDisplayProductsTemplate.opsForValue().get(LOWEST_FULL_CATEGORY_KEY);
    }

    public void forceRefreshProductList(List<DisplayProduct> lowestProductsByCategory) {
        redisDisplayProductsTemplate.opsForValue().set(LOWEST_FULL_CATEGORY_KEY, lowestProductsByCategory);
    }
    private boolean isSameCategory(List<DisplayProduct> products, DisplayProduct displayProduct) {
        return products.stream().anyMatch(product -> product.getCategoryId() == displayProduct.getCategoryId());
    }
    private boolean isChange(DisplayProduct newProduct, DisplayProduct product) {
        return newProduct.getCategoryId() == product.getCategoryId()
            && newProduct.getProductPrice() < product.getProductPrice()
            && newProduct.getLastUpdatedDateTime().isAfter(product.getLastUpdatedDateTime());
    }
    private boolean isSame(DisplayProduct newProduct, DisplayProduct product) {
        return newProduct.getProductId() == product.getProductId()
            && !newProduct.getLastUpdatedDateTime().isBefore(product.getLastUpdatedDateTime());
    }

}
