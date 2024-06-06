package com.cody.backend.storage.service;

import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.db.BrandService;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.DisplayProduct;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandStorageService {
    private final BrandService brandService;

    public List<BrandRequest> insertBrands(List<DisplayProduct> products) throws DataIntegrityViolationException, IllegalStateException {
        List<BrandRequest> brandRequests = DisplayProductConverter.convertToBrandRequestDTO(MethodType.INSERT, products);

        List<BrandDTO> brands = brandService.insertAll(brandRequests);
        return brands.stream()
                     .map(brand -> BrandRequest.dtoBuilder(brand, MethodType.INSERT))
                     .collect(Collectors.toList());
    }

    public List<BrandRequest> deleteBrands(List<DisplayProduct> products) throws EmptyResultDataAccessException, EntityNotFoundException, IllegalStateException, IllegalArgumentException {
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, products);

        brandService.deleteBrands(brands);
        return brands.stream().map(brand -> BrandRequest.dtoBuilder(brand, MethodType.DELETE))
                     .collect(
                         Collectors.toList());
    }

    public List<BrandRequest> updateBrands(List<DisplayProduct> products) throws EntityNotFoundException, OptimisticLockingFailureException, IllegalStateException {
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, products);

        List<BrandDTO> brandDTOs = brandService.updateBrands(brands);
        return brandDTOs.stream()
                        .map(brand -> BrandRequest.dtoBuilder(brand, MethodType.UPDATE))
                        .collect(Collectors.toList());
    }
}
