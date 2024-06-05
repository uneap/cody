package com.cody.backend.storage.service;

import com.cody.common.core.MethodType;
import com.cody.domain.store.product.db.ProductService;
import com.cody.domain.store.product.dto.ProductDTO;
import com.cody.domain.store.product.dto.ProductRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStorageService {
    private final ProductService productService;

    public List<ProductRequestDTO> insertProducts(List<ProductRequestDTO> productRequestDTOs) throws DataIntegrityViolationException, IllegalStateException {
        List<ProductDTO> products = new ArrayList<>();
        for (ProductRequestDTO productRequestDTO : productRequestDTOs){
            products.add(productService.insert(productRequestDTO));
        }
        return products.stream()
                     .map(product -> ProductRequestDTO.dtoBuilder(product, MethodType.INSERT))
                     .collect(Collectors.toList());
    }

    public List<ProductRequestDTO> deleteProducts(List<ProductRequestDTO> productRequestDTOs) throws EmptyResultDataAccessException, EntityNotFoundException, IllegalStateException, IllegalArgumentException {
        List<ProductDTO> products = new ArrayList<>();
        for (ProductRequestDTO productRequestDTO : productRequestDTOs){
            products.add(productService.insert(productRequestDTO));
        }
        return products.stream()
                       .map(product -> ProductRequestDTO.dtoBuilder(product, MethodType.INSERT))
                       .collect(Collectors.toList());
    }

    public List<ProductRequestDTO> updateProducts(List<ProductRequestDTO> productRequestDTOs) throws EntityNotFoundException, OptimisticLockingFailureException, IllegalStateException {
        List<ProductDTO> products = productService.updateProducts(productRequestDTOs);
        return products.stream()
                        .map(product -> ProductRequestDTO.dtoBuilder(product, MethodType.UPDATE))
                        .collect(Collectors.toList());
    }
}
