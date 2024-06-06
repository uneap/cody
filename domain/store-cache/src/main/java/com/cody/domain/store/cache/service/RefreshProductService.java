package com.cody.domain.store.cache.service;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.PriceLevel;
import com.cody.domain.store.cache.service.redis.BrandCategoryFullProductService;
import com.cody.domain.store.cache.service.redis.LowestFullCategoryService;
import com.cody.domain.store.cache.service.redis.LowestPriceBrandIdService;
import com.cody.domain.store.cache.service.redis.PriceBrandService;
import com.cody.domain.store.cache.service.redis.PriceCategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class RefreshProductService {
    private final PriceBrandService priceBrandService;
    private final LowestPriceBrandIdService lowestPriceBrandIdService;
    private final BrandCategoryFullProductService brandCategoryFullProductService;
    private final PriceCategoryService priceCategoryService;
    private final LowestFullCategoryService lowestFullCategoryService;

    public void deleteBrandInCache(long brandId) {
        brandCategoryFullProductService.removeBrand(brandId);
        priceBrandService.removeBrand(brandId);
        lowestPriceBrandIdService.removeBrand(brandId);
        priceCategoryService.removeBrand(brandId);
        List<DisplayProduct> products = lowestFullCategoryService.get();
        List<Long> categoryIds = products.stream().filter(product -> product.getBrandId() == brandId)
                                         .map(DisplayProduct::getCategoryId)
                                         .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(categoryIds)) {
            return;
        }
        for (long categoryId :categoryIds){
            Set<TypedTuple<String>> brandIdAndPriceSet = priceCategoryService.getBrandIdAndPrice(categoryId, PriceLevel.LOWEST);
            if(CollectionUtils.isEmpty(brandIdAndPriceSet)) {
                continue;
            }
            TypedTuple<String> brandIdAndPrice = brandIdAndPriceSet.stream().findFirst().get();
            DisplayProduct newProduct = brandCategoryFullProductService.getLowestByBrandAndCategory(Long.parseLong(brandIdAndPrice.getValue()), categoryId);
            products.replaceAll(product-> product.getCategoryId() == categoryId ? newProduct : product);
        }
    }

    public void addProductInCache(DisplayProduct product) {
        List<DisplayProduct> products = lowestPriceBrandIdService.get(product.getBrandId());
        if (CollectionUtils.isEmpty(products)) {
            addLowestPriceBrand(product, new ArrayList<>());
            addLowHighProduct(product.getBrandId(), product.getCategoryId());
            return;
        }
        if(isNewLowest(product, products) || isNewCategory(product, products)) {
            addLowestPriceBrand(product, products);
            addLowHighProduct(product.getBrandId(), product.getCategoryId());
            return;
        }
        brandCategoryFullProductService.addProduct(product);
        addLowHighProduct(product.getBrandId(), product.getCategoryId());
    }

    public void updateProductInCache(DisplayProduct product, DisplayProduct oldProduct) {
        brandCategoryFullProductService.updateProduct(product, oldProduct);
        DisplayProduct newLowestProduct = brandCategoryFullProductService.getLowestByBrandAndCategory(product.getBrandId(), product.getCategoryId());
        List<DisplayProduct> products = lowestPriceBrandIdService.updateProduct(newLowestProduct);

        if (CollectionUtils.isEmpty(products)) {
            throw new NoSuchElementException("UPDATE OBJECT NOT FOUND");
        }
        long totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        priceBrandService.refreshRank(product.getBrandId(), totalPrice, product.getLastUpdatedDateTime());
        refreshLowHighProduct(newLowestProduct, oldProduct);
    }

    public void deleteProductInCache(DisplayProduct oldProduct) {
        brandCategoryFullProductService.removeProduct(oldProduct);
        DisplayProduct newLowestProduct = brandCategoryFullProductService.getLowestByBrandAndCategory(oldProduct.getBrandId(), oldProduct.getCategoryId());
        List<DisplayProduct> products = lowestPriceBrandIdService.updateProduct(newLowestProduct);

        if (CollectionUtils.isEmpty(products)) {
            throw new NoSuchElementException("DELETE OBJECT NOT FOUND");
        }

        long totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        priceBrandService.refreshRank(oldProduct.getBrandId(), totalPrice, oldProduct.getLastUpdatedDateTime());
        refreshLowHighProduct(newLowestProduct, oldProduct);
    }
    private boolean isNewCategory(DisplayProduct newProduct, List<DisplayProduct> products) {
        return products.stream().noneMatch(product -> product.getCategoryId() == newProduct.getCategoryId());
    }

    private boolean isNewLowest(DisplayProduct addProduct, List<DisplayProduct> products) {
        return products.stream()
                       .filter(product -> product.getCategoryId() == addProduct.getCategoryId())
                       .noneMatch(product -> product.getProductPrice() < addProduct.getProductPrice());
    }
    private void addLowestPriceBrand(DisplayProduct displayProduct, List<DisplayProduct> products) {
        lowestPriceBrandIdService.refreshProductList(displayProduct, products);
        long totalPrice = displayProduct.getProductPrice();
        if(!CollectionUtils.isEmpty(products)) {
            totalPrice = products.stream().mapToLong(DisplayProduct::getProductPrice).sum();
        }
        priceBrandService.refreshRank(displayProduct.getBrandId(), totalPrice, displayProduct.getLastUpdatedDateTime());
        brandCategoryFullProductService.addProduct(displayProduct);
    }

    private void addLowHighProduct(long brandId, long categoryId) {
        DisplayProduct lowestProduct = brandCategoryFullProductService.getLowestByBrandAndCategory(brandId, categoryId);
        DisplayProduct highestProduct = brandCategoryFullProductService.getHighestByBrandAndCategory(brandId, categoryId);
        priceCategoryService.refreshProduct(lowestProduct, PriceLevel.LOWEST);
        priceCategoryService.refreshProduct(highestProduct, PriceLevel.HIGHEST);
        lowestFullCategoryService.addProductList(lowestProduct);
    }

    private void refreshLowHighProduct(DisplayProduct lowestProduct, DisplayProduct oldProduct) {
        DisplayProduct highestProduct = brandCategoryFullProductService.getHighestByBrandAndCategory(oldProduct.getBrandId(), oldProduct.getCategoryId());
        priceCategoryService.refreshProduct(lowestProduct, PriceLevel.LOWEST);
        priceCategoryService.refreshProduct(highestProduct, PriceLevel.HIGHEST);
        lowestFullCategoryService.update(oldProduct, lowestProduct);
    }
}
