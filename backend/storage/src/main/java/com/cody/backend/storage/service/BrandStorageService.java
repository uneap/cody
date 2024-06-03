package com.cody.backend.storage.service;

import com.cody.backend.storage.sender.BrandKafkaSender;
import com.cody.common.core.MethodType;
import com.cody.domain.brand.BrandService;
import com.cody.domain.brand.dto.BrandDTO;
import com.cody.domain.brand.dto.BrandRequestDTO;
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

    private final BrandKafkaSender brandKafkaSender;
    private final BrandService brandService;

    public void insertBrands(List<BrandRequestDTO> brandRequests) throws DataIntegrityViolationException, IllegalStateException {
        List<BrandDTO> brands = brandService.insertAll(brandRequests);
        List<BrandRequestDTO> requests = brands.stream()
                                               .map(brand -> BrandRequestDTO.dtoBuilder(brand, MethodType.INSERT))
                                               .collect(Collectors.toList());
        brandKafkaSender.sendBrands(requests);
    }

    public void deleteBrands(List<BrandRequestDTO> brands) throws EmptyResultDataAccessException, EntityNotFoundException, IllegalStateException, IllegalArgumentException {
        brandService.deleteBrands(brands);
        brandKafkaSender.sendBrands(brands);
    }

    public void updateBrands(List<BrandRequestDTO> brands)
        throws EntityNotFoundException, OptimisticLockingFailureException, IllegalStateException {
        List<BrandDTO> brandDTOs = brandService.updateBrands(brands);
        List<BrandRequestDTO> requests = brandDTOs.stream()
                                                  .map(brand -> BrandRequestDTO.dtoBuilder(brand, MethodType.UPDATE))
                                                  .collect(Collectors.toList());
        brandKafkaSender.sendBrands(requests);
    }
}
