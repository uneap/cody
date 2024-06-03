package com.cody.domain.store.product.db;

import com.cody.domain.store.brand.db.BrandDAO;
import com.cody.domain.store.brand.db.BrandRepository;
import com.cody.domain.store.category.db.CategoryDAO;
import com.cody.domain.store.category.db.CategoryRepository;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public ProductDTO insert(ProductRequestDTO productRequestDTO) throws DataIntegrityViolationException {
        CategoryDAO categoryDAO = categoryRepository.getReferenceById(productRequestDTO.getCategoryId());
        BrandDAO brandDAO = brandRepository.getReferenceById(productRequestDTO.getBrandId());
        ProductDAO productDAO = new ProductDAO(productRequestDTO, categoryDAO, brandDAO);
        productDAO = productRepository.save(productDAO);
        return ProductDTO.daoBuilder(productDAO);
    }

    @Transactional
    public List<ProductDTO> updateProducts(List<ProductRequestDTO> productDTOs) throws OptimisticLockingFailureException,
        InvalidDataAccessApiUsageException, EntityNotFoundException, DataIntegrityViolationException {
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
    public ProductDTO updateProduct(ProductRequestDTO product) throws OptimisticLockingFailureException, EntityNotFoundException {
        ProductDAO productDAO = convertUpdatedBrandData(product);
        productDAO = productRepository.save(productDAO);
        return ProductDTO.daoBuilder(productDAO);
    }

    @Transactional
    public void deleteProduct(ProductRequestDTO productDTO) throws EmptyResultDataAccessException {
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
