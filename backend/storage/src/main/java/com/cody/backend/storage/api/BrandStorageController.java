package com.cody.backend.storage.api;

import com.cody.backend.storage.request.StorageRequest;
import com.cody.backend.storage.response.BrandResponse;
import com.cody.backend.storage.sender.BrandKafkaSender;
import com.cody.backend.storage.service.BrandFacadeService;
import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.AllUser;
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

    private final BrandFacadeService brandFacadeService;
    private final BrandKafkaSender brandKafkaSender;

    // 현재 유저 추가기능은 없으므로, user와 seller 추가 시, brand name과 동일하게 설정, 그러나 업데이트 시 이름은 변경되지 않음.
    // brand 삭제 시, user는 그대로 두고 seller만 삭제 됨.
    @PostMapping(value = "/insert")
    public BrandResponse insertBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> insertedBrands = brandFacadeService.insertBrands(products);
        List<AllUser> insertedAllUsers = brandFacadeService.insertAllUser(insertedBrands);
        List<DisplayProductRequest> insertedProducts = DisplayProductConverter.convertBrandToDisplayProduct(
            insertedAllUsers, products, insertedBrands, MethodType.INSERT);
        brandKafkaSender.sendBrands(insertedProducts);

        return new BrandResponse(insertedProducts, products);
    }

    @DeleteMapping(value = "/delete")
    public BrandResponse deleteBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> deletedBrands = brandFacadeService.deleteBrands(products);
        List<AllUser> deletedAllUsers = brandFacadeService.deleteAllUser(deletedBrands);
        List<DisplayProductRequest> deletedProducts = DisplayProductConverter.convertBrandToDisplayProduct(
            deletedAllUsers, products, deletedBrands, MethodType.DELETE);
        brandKafkaSender.sendBrands(deletedProducts);

        return new BrandResponse(deletedProducts, products);

    }

    @PutMapping(value = "/update")
    public BrandResponse updateBrands(@RequestBody StorageRequest request) {
        List<DisplayProduct> products = request.getDisplayProducts();
        List<BrandRequest> updatedBrands = brandFacadeService.updateBrands(products);
        List<DisplayProductRequest> updatedProducts = DisplayProductConverter.convertBrandToDisplayProduct(
            products, updatedBrands, MethodType.UPDATE);
        brandKafkaSender.sendBrands(updatedProducts);

        return new BrandResponse(updatedProducts, products);
    }
}
