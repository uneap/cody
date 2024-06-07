package com.cody.backend.storage.service;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.db.BrandService;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
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

    public List<BrandRequest> insertBrands(List<BrandRequest> brandRequests) throws DataIntegrityViolationException, IllegalStateException {
        List<BrandDTO> brands = brandService.insertAll(brandRequests);
        return brands.stream()
                     .map(brand -> BrandRequest.dtoBuilder(brand, MethodType.INSERT))
                     .collect(Collectors.toList());
    }

    public List<BrandRequest> deleteBrands(List<BrandRequest> brandRequests) throws EmptyResultDataAccessException, EntityNotFoundException, IllegalStateException, IllegalArgumentException {
        brandService.deleteBrands(brandRequests);
        return brandRequests.stream().map(brand -> BrandRequest.dtoBuilder(brand, MethodType.DELETE))
                     .collect(
                         Collectors.toList());
    }

    public List<BrandRequest> updateBrands(List<BrandRequest> brandRequests) throws EntityNotFoundException, OptimisticLockingFailureException, IllegalStateException {
        List<BrandDTO> brandDTOs = brandService.updateBrands(brandRequests);
        return brandDTOs.stream()
                        .map(brand -> BrandRequest.dtoBuilder(brand, MethodType.UPDATE))
                        .collect(Collectors.toList());
    }
}
