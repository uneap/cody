package com.cody.backend.storage.service;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.common.core.MethodType;
import com.cody.domain.store.product.db.ProductService;
import com.cody.domain.store.product.dto.ProductDTO;
import com.cody.domain.store.product.dto.ProductRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        List<ProductDTO> products = productService.findAllById(productRequestDTOs.stream().map(ProductRequestDTO::getId).toList());
        if(CollectionUtils.isEmpty(products)) {
            throw new EntityNotFoundException();
        }
        Map<Long, ProductDTO> requestProducts = products.stream().collect(Collectors.toMap(ProductDTO::getId, Function.identity()));
        for (ProductRequestDTO productRequestDTO : productRequestDTOs){
            productService.deleteProduct(productRequestDTO);
        }
        return productRequestDTOs.stream()
            .peek(product -> requestProducts.get(product.getId()))
                                 .peek(product -> product.setLastModifiedDate(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))))
                                 .peek(product -> product.setOldProduct(ProductRequestDTO.dtoBuilder(requestProducts.get(product.getId()), MethodType.DELETE)))
                       .collect(Collectors.toList());
    }

    public List<ProductRequestDTO> updateProducts(List<ProductRequestDTO> productRequestDTOs) throws EntityNotFoundException, OptimisticLockingFailureException, IllegalStateException {
        List<ProductDTO> products = productService.findAllById(productRequestDTOs.stream().map(ProductRequestDTO::getId).toList());
        if(CollectionUtils.isEmpty(products)) {
            throw new EntityNotFoundException();
        }
        Map<Long, ProductDTO> requestProducts = products.stream().collect(Collectors.toMap(ProductDTO::getId, Function.identity()));
        List<ProductDTO> updatedProducts = productService.updateProducts(productRequestDTOs);

        return updatedProducts.stream()
                              .map(product -> ProductRequestDTO.dtoBuilder(product, MethodType.UPDATE))
                              .peek(product -> product.setOldProduct(ProductRequestDTO.dtoBuilder(requestProducts.get(product.getId()), MethodType.DELETE)))
                        .collect(Collectors.toList());
    }
}
