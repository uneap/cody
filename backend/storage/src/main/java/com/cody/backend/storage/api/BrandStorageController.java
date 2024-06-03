package com.cody.backend.storage.api;

import com.cody.backend.storage.request.BrandRequest;
import com.cody.backend.storage.response.BrandResponse;
import com.cody.backend.storage.service.BrandStorageService;
import com.cody.common.core.MethodType;
import com.cody.domain.brand.dto.BrandRequestDTO;
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

    private final BrandStorageService brandStorageService;

    @PostMapping(value = "/insert")
    public BrandResponse insertBrands(@RequestBody BrandRequest brandRequest) {
        List<BrandRequestDTO> brands = brandRequest.convertToKafkaVersion(MethodType.INSERT);
        brandStorageService.insertBrands(brands);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }

    @DeleteMapping(value = "/delete")
    public BrandResponse deleteBrands(@RequestBody BrandRequest brandRequest) {
        List<BrandRequestDTO> brands = brandRequest.convertToKafkaVersion(MethodType.DELETE);
        brandStorageService.deleteBrands(brands);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }
    @PutMapping(value = "/update")
    public BrandResponse updateBrands(@RequestBody BrandRequest brandRequest) {
        List<BrandRequestDTO> brands = brandRequest.convertToKafkaVersion(MethodType.UPDATE);
        brandStorageService.updateBrands(brands);
        return BrandResponse.builder()
                            .statusCode(200)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .build();
    }
}
