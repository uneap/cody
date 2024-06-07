package com.cody.backend.storage.api;

import com.cody.backend.storage.producer.BrandKafkaSender;
import com.cody.backend.storage.request.BrandStorageRequest;
import com.cody.backend.storage.response.BrandResponse;
import com.cody.backend.storage.service.BrandStorageService;
import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.backend.storage.util.ValidRequestChecker;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/cody/v1/brand/storage")
@RequiredArgsConstructor
public class BrandStorageController {
    private final BrandStorageService brandStorageService;
    private final BrandKafkaSender brandKafkaSender;
    private final ValidRequestChecker validRequestChecker;

    @PostMapping(value = "/insert")
    public BrandResponse insertBrands(@RequestBody BrandStorageRequest request) {
        validRequestChecker.isNoneValidByBrand(request, MethodType.INSERT);
        List<BrandDTO> brandDTOs = request.getBrands();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.INSERT, brandDTOs);
        List<BrandRequest> insertedBrands = brandStorageService.insertBrands(brands);
        brandKafkaSender.sendBrands(insertedBrands);

        return new BrandResponse(insertedBrands, brandDTOs);
    }

    @DeleteMapping(value = "/delete")
    public BrandResponse deleteBrands(@RequestBody BrandStorageRequest request) {
        validRequestChecker.isNoneValidByBrand(request, MethodType.DELETE);
        List<BrandDTO> brandDTOs = request.getBrands();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, brandDTOs);
        List<BrandRequest> deletedBrands = brandStorageService.deleteBrands(brands);
        brandKafkaSender.sendBrands(deletedBrands);

        return new BrandResponse(deletedBrands, brandDTOs);

    }

    @PutMapping(value = "/update")
    public BrandResponse updateBrands(@RequestBody BrandStorageRequest request) {
        validRequestChecker.isNoneValidByBrand(request, MethodType.UPDATE);
        List<BrandDTO> brandDTOs = request.getBrands();
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.UPDATE, brandDTOs);
        List<BrandRequest> updatedBrands = brandStorageService.updateBrands(brands);
        brandKafkaSender.sendBrands(updatedBrands);

        return new BrandResponse(updatedBrands, brandDTOs);
    }
}
