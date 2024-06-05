package com.cody.backend.storage.api;

import com.cody.backend.storage.request.StorageRequest;
import com.cody.backend.storage.response.BrandResponse;
import com.cody.backend.storage.sender.BrandKafkaSender;
import com.cody.backend.storage.service.BrandStorageService;
import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final BrandKafkaSender brandKafkaSender;
    private final BrandStorageService brandStorageService;

    @PostMapping(value = "/insert")
    public BrandResponse insertBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.INSERT, products);
        List<BrandRequest> insertedBrands = brandStorageService.insertBrands(brands);
        List<DisplayProductRequest> insertedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, insertedBrands, MethodType.INSERT);
        brandKafkaSender.sendBrands(insertedProducts);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }

    @DeleteMapping(value = "/delete")
    public BrandResponse deleteBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, products);
        List<BrandRequest> insertedBrands = brandStorageService.deleteBrands(brands);
        List<DisplayProductRequest> insertedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, insertedBrands, MethodType.DELETE);
        brandKafkaSender.sendBrands(insertedProducts);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }
    @PutMapping(value = "/update")
    public BrandResponse updateBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.UPDATE, products);
        List<BrandRequest> insertedBrands = brandStorageService.updateBrands(brands);
        List<DisplayProductRequest> insertedProducts = DisplayProductConverter.convertBrandToDisplayProduct(products, insertedBrands, MethodType.UPDATE);
        brandKafkaSender.sendBrands(insertedProducts);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }
}
