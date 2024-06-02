package com.cody.domain.brand;

import com.cody.domain.brand.db.BrandDAO;
import com.cody.domain.brand.db.BrandRepository;
import com.cody.domain.brand.dto.BrandDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public List<BrandDAO> insertAll(List<BrandDTO> brands) throws DataIntegrityViolationException {
        List<BrandDAO> brandDAOs = brands.stream().map(BrandDAO::new).toList();
        return brandRepository.saveAll(brandDAOs);
    }

    @Transactional
    public BrandDAO insert(BrandDTO brand) throws DataIntegrityViolationException {
        BrandDAO brandDAO = new BrandDAO(brand);
        return brandRepository.save(brandDAO);
    }
    @Transactional
    public List<BrandDAO> updateBrands(List<BrandDTO> brandDTOs) throws OptimisticLockingFailureException {
        List<BrandDAO> brandDAOs = brandDTOs.stream()
                                            .map(this::convertUpdatedBrandData)
                                            .collect(Collectors.toList());
        return brandRepository.saveAll(brandDAOs);
    }

    public BrandDAO convertUpdatedBrandData(BrandDTO brandDTO) {
        Optional<BrandDAO> brandOptional = brandRepository.findById(brandDTO.getId());
        if(brandOptional.isEmpty()) {
            return null;
        }
        BrandDAO brandDAO = brandOptional.get();
        brandDAO.changeData(brandDTO);
        return brandDAO;
    }
    @Transactional
    public BrandDAO updateBrand(BrandDTO brandDTO) throws OptimisticLockingFailureException {
        BrandDAO brandDAO = convertUpdatedBrandData(brandDTO);
        return brandRepository.save(brandDAO);
    }

    @Transactional
    public void deleteBrands(List<BrandDTO> brandDTOs) {
        List<Long> ids = brandDTOs.stream().map(BrandDTO::getId).collect(Collectors.toList());
        brandRepository.deleteAllById(ids);
    }

    @Transactional
    public void deleteBrand(BrandDTO brandDTO) {
        brandRepository.deleteById(brandDTO.getId());
    }

    public List<BrandDTO> findAllById(List<Long> ids) {
        List<BrandDAO> brands = brandRepository.findAllById(ids);
        return brands.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<BrandDTO> findAll() {
        List<BrandDAO> brands = brandRepository.findAll();
        return brands.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
    }

    public BrandDTO findById(long id) {
        Optional<BrandDAO> brandDAO = brandRepository.findById(id);
        return brandDAO.map(BrandDTO::daoBuilder).orElse(null);
    }
}
