package com.cody.domain.brand;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

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
    public BrandDAO updateName(BrandDTO brandDTO) throws OptimisticLockingFailureException {
        Optional<BrandDAO> brandOptional = brandRepository.findById(brandDTO.getId());
        if(brandOptional.isEmpty()) {
            return null;
        }
        BrandDAO brandDAO = brandOptional.get();
        brandDAO.changeName(brandDTO.getName());
        return brandRepository.save(brandDAO);
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
