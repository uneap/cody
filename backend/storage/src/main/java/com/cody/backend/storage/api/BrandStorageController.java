package com.cody.backend.storage.api;

import com.cody.backend.storage.request.StorageRequest;
import com.cody.backend.storage.response.BrandResponse;
import com.cody.backend.storage.sender.BrandKafkaSender;
import com.cody.backend.storage.service.BrandStorageService;
import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.backend.storage.util.ValidRequestChecker;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cody/v1/brand/storage")
@RequiredArgsConstructor
public class BrandStorageController {
    private final BrandStorageService brandStorageService;
    private final BrandKafkaSender brandKafkaSender;
    private final ValidRequestChecker validRequestChecker;

    @PostMapping(value = "/insert")
    public BrandResponse insertBrands(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> insertedBrands = brandStorageService.insertBrands(products);
        List<DisplayProductRequest> insertedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, insertedBrands, MethodType.INSERT);
        brandKafkaSender.sendBrands(insertedProducts);

        return new BrandResponse(insertedProducts, products);
    }

    @DeleteMapping(value = "/delete")
    public BrandResponse deleteBrands(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> deletedBrands = brandStorageService.deleteBrands(products);
        List<DisplayProductRequest> deletedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, deletedBrands, MethodType.DELETE);
        brandKafkaSender.sendBrands(deletedProducts);

        return new BrandResponse(deletedProducts, products);

    }

    @PutMapping(value = "/update")
    public BrandResponse updateBrands(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> updatedBrands = brandStorageService.updateBrands(products);
        List<DisplayProductRequest> updatedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, updatedBrands, MethodType.UPDATE);
        brandKafkaSender.sendBrands(updatedProducts);

        return new BrandResponse(updatedProducts, products);
    }
}
