package com.cody.domain.store.cache.service;

import static com.cody.domain.store.cache.constants.constants.PRICE_CATEGORY;

import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class LowestPriceCategoryService {

    private final RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate;
    private final RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate;
    private static final String LOWEST_PRICE_CATEGORY_KEY = "price";

    public void refreshLowestPriceCategoryToUpdate(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = getLowestPriceProducts();
        addLowestPriceCategoryItem(product);
        DisplayProduct sameProduct = getSameProduct(lowestPriceProducts, product.getCategoryId());
        if(sameProduct == null) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product.getCategoryId());
    }

    public void refreshLowestPriceCategoryToDelete(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = getLowestPriceProducts();
        redisDisplayProductTemplate.opsForZSet().remove(String.format(PRICE_CATEGORY, product.getCategoryId()), product);
        DisplayProduct sameProduct = getSameProduct(lowestPriceProducts, product.getCategoryId());
        if(sameProduct == null) {
            return;
        }
        refreshLatestRanking(lowestPriceProducts, product.getCategoryId());
    }

    public void refreshLowestPriceCategoryToAdd(DisplayProduct product) {
        List<DisplayProduct> lowestPriceProducts = getLowestPriceProducts();
        addLowestPriceCategoryItem(product);
        DisplayProduct lowestPriceProductByCategory = getLowestPriceProductByCategory(lowestPriceProducts, product.getCategoryId());
        if (lowestPriceProductByCategory == null || lowestPriceProductByCategory.getProductPrice() > product.getProductPrice()) {
            addLowestPriceProductByCategory(lowestPriceProducts, product);
        }
    }

    private void refreshLatestRanking(List<DisplayProduct> lowestPriceProducts, long categoryId) {
        Set<DisplayProduct> lowestPriceProductByCategory = getLowestPriceProductByCategory(categoryId);
        if(CollectionUtils.isEmpty(lowestPriceProductByCategory)) {
            return;
        }
        addLowestPriceProductByCategory(lowestPriceProducts, lowestPriceProductByCategory.stream().findFirst().get());
    }
    private void addLowestPriceProductByCategory(List<DisplayProduct> lowestPriceProducts, DisplayProduct product) {
        lowestPriceProducts = lowestPriceProducts.stream()
                                                 .filter(lowestPriceProduct -> lowestPriceProduct.getCategoryId() != lowestPriceProduct.getCategoryId())
                                                 .collect(Collectors.toList());
        lowestPriceProducts.add(product);
        redisDisplayProductsTemplate.opsForValue().set(LOWEST_PRICE_CATEGORY_KEY, lowestPriceProducts);
    }
    private List<DisplayProduct> getLowestPriceProducts() {
        return redisDisplayProductsTemplate.opsForValue().get(LOWEST_PRICE_CATEGORY_KEY);
    }

    private Set<DisplayProduct> getLowestPriceProductByCategory(long categoryId) {
        return redisDisplayProductTemplate.opsForZSet().range(String.format(PRICE_CATEGORY, categoryId), 0, 0);
    }

    private DisplayProduct getLowestPriceProductByCategory(List<DisplayProduct> products, long categoryId) {
        if (CollectionUtils.isEmpty(products)) {
            return null;
        }
        return products.stream().filter(product -> product.getCategoryId() == categoryId)
                       .findFirst().orElse(null);

    }

    private DisplayProduct getSameProduct(List<DisplayProduct> products, long productId) {
        if (CollectionUtils.isEmpty(products)) {
            return null;
        }
        return products.stream().filter(product -> product.getProductId() == productId)
                       .findFirst().orElse(null);

    }

    private void addLowestPriceCategoryItem(DisplayProduct product) {
        redisDisplayProductTemplate.opsForZSet()
                                   .add(String.format(PRICE_CATEGORY, product.getCategoryId()),
                                       product, product.getProductPrice());
    }
}
