package com.cody.backend.display.api;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.LowHighProduct;
import com.cody.domain.store.cache.service.LowHighPriceCategoryService;
import com.cody.domain.store.cache.service.LowestPriceBrandService;
import com.cody.domain.store.cache.service.LowestPriceCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cody/v1/display")
@RequiredArgsConstructor
public class DisplayController {

   private final LowestPriceBrandService lowestPriceBrandService;
   private final LowestPriceCategoryService lowestPriceCategoryService;
   private final LowHighPriceCategoryService lowHighPriceCategoryService;

    @GetMapping(value = "/lowest/price/brand")
    public List<DisplayProduct> getLowestPriceBrandProducts() {
        return lowestPriceBrandService.getLowestPriceBrand();
    }

    @GetMapping(value = "/lowest/price/category")
    public List<DisplayProduct> getLowestPriceCategoryProducts() {
        return lowestPriceCategoryService.getLowestPriceCategories();
    }

    @GetMapping(value = "/low/high/price/category")
    public LowHighProduct getLowHighPriceCategoryProducts(@RequestParam(defaultValue = "1")long categoryId) {
        return lowHighPriceCategoryService.getLowHighProduct(categoryId);
    }
}
