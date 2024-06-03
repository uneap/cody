package com.cody.domain.store.product.db;

import com.cody.domain.store.product.dto.ProductDTO;
import com.cody.domain.store.product.dto.ProductRequestDTO;
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
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @Transactional
    public List<ProductDTO> insertAll(List<ProductRequestDTO> brands) throws DataIntegrityViolationException {
        List<ProductDAO> productDAOs = brands.stream().map(ProductDAO::new).toList();
        productDAOs = productRepository.saveAll(productDAOs);
        if(!CollectionUtils.isEmpty(productDAOs)) {
            return productDAOs.stream().map(ProductDTO::daoBuilder).collect(Collectors.toList());
        }
        throw new DataIntegrityViolationException("insert fail");
    }

    @Transactional
    public ProductDTO insert(ProductRequestDTO productRequestDTO) throws DataIntegrityViolationException {
        ProductDAO productDAO = new ProductDAO(productRequestDTO);
        productDAO = productRepository.save(productDAO);
        return ProductDTO.daoBuilder(productDAO);
    }

    @Transactional
    public List<ProductDTO> updateBrands(List<ProductRequestDTO> productDTOs) throws OptimisticLockingFailureException, EntityNotFoundException {
        List<ProductDAO> productDAOs = productDTOs.stream()
                                            .map(this::convertUpdatedBrandData)
                                            .collect(Collectors.toList());
        productDAOs = productRepository.saveAll(productDAOs);
        if(!CollectionUtils.isEmpty(productDAOs)) {
            return productDAOs.stream().map(ProductDTO::daoBuilder).collect(Collectors.toList());
        }
        throw new DataIntegrityViolationException("update fail");
    }

    public ProductDAO convertUpdatedBrandData(ProductRequestDTO product) throws EntityNotFoundException  {
        Optional<ProductDAO> productOptional = productRepository.findById(product.getId());
        if(productOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        ProductDAO productDAO = productOptional.get();
        productDAO.changeData(product);
        return productDAO;
    }
    @Transactional
    public ProductDTO updateBrand(ProductRequestDTO product) throws OptimisticLockingFailureException, EntityNotFoundException {
        ProductDAO productDAO = convertUpdatedBrandData(product);
        productDAO = productRepository.save(productDAO);
        return ProductDTO.daoBuilder(productDAO);
    }

    @Transactional
    public void deleteBrands(List<ProductRequestDTO> products) throws EmptyResultDataAccessException, IllegalArgumentException {
        List<Long> ids = products.stream().map(ProductRequestDTO::getId).collect(Collectors.toList());
        productRepository.deleteAllById(ids);
    }

    @Transactional
    public void deleteBrand(ProductRequestDTO productDTO) throws EmptyResultDataAccessException {
        productRepository.deleteById(productDTO.getId());
    }
    public List<ProductDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<ProductDAO> brands = productRepository.findAllById(ids);
        return brands.stream().map(ProductDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<ProductDTO> findAll() throws NoSuchElementException {
        List<ProductDAO> brands = productRepository.findAll();
        return brands.stream().map(ProductDTO::daoBuilder).collect(Collectors.toList());
    }

    public ProductDTO findById(long id) throws NoSuchElementException {
        Optional<ProductDAO> brandDAO = productRepository.findById(id);
        return brandDAO.map(ProductDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
