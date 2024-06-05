package com.cody.domain.store.brand.db;

import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public List<BrandDTO> insertAll(List<BrandRequest> brands) throws DataIntegrityViolationException {
        List<BrandDAO> brandDAOs = brands.stream().map(BrandDAO::new).toList();
        brandDAOs = brandRepository.saveAll(brandDAOs);
        if(!CollectionUtils.isEmpty(brandDAOs)) {
            return brandDAOs.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
        }
        throw new DataIntegrityViolationException("insert fail");
    }

    @Transactional
    public BrandDTO insert(BrandRequest brand) throws DataIntegrityViolationException {
        BrandDAO brandDAO = new BrandDAO(brand);
        brandDAO = brandRepository.save(brandDAO);
        return BrandDTO.daoBuilder(brandDAO);
    }
    @Transactional
    public List<BrandDTO> updateBrands(List<BrandRequest> brandDTOs) throws OptimisticLockingFailureException, EntityNotFoundException, DataIntegrityViolationException {
        List<BrandDAO> brandDAOs = brandDTOs.stream()
                                            .map(this::convertUpdatedBrandData)
                                            .collect(Collectors.toList());
        brandDAOs = brandRepository.saveAll(brandDAOs);
        if(!CollectionUtils.isEmpty(brandDAOs)) {
            return brandDAOs.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
        }
        throw new DataIntegrityViolationException("update fail");
    }

    public BrandDAO convertUpdatedBrandData(BrandRequest brandDTO) throws EntityNotFoundException  {
        Optional<BrandDAO> brandOptional = brandRepository.findById(brandDTO.getId());
        if(brandOptional.isEmpty()) {
             throw new EntityNotFoundException();
        }
        BrandDAO brandDAO = brandOptional.get();
        brandDAO.changeData(brandDTO);
        return brandDAO;
    }
    @Transactional
    public BrandDTO updateBrand(BrandRequest brandDTO) throws OptimisticLockingFailureException, EntityNotFoundException {
        BrandDAO brandDAO = convertUpdatedBrandData(brandDTO);
        brandDAO = brandRepository.save(brandDAO);
        return BrandDTO.daoBuilder(brandDAO);
    }

    @Transactional
    public void deleteBrands(List<BrandRequest> brandDTOs) throws EmptyResultDataAccessException, IllegalArgumentException {
        List<Long> ids = brandDTOs.stream().map(BrandRequest::getId).collect(Collectors.toList());
        brandRepository.deleteAllById(ids);
    }

    @Transactional
    public void deleteBrand(BrandRequest brandDTO) throws EmptyResultDataAccessException, InvalidDataAccessApiUsageException {
        brandRepository.deleteById(brandDTO.getId());
    }

    public List<BrandDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<BrandDAO> brands = brandRepository.findAllById(ids);
        return brands.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<BrandDTO> findAll() throws NoSuchElementException {
        List<BrandDAO> brands = brandRepository.findAll();
        return brands.stream().map(BrandDTO::daoBuilder).collect(Collectors.toList());
    }

    public BrandDTO findById(long id) throws NoSuchElementException {
        Optional<BrandDAO> brandDAO = brandRepository.findById(id);
        return brandDAO.map(BrandDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
