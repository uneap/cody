package com.cody.backend.storage.api;

import com.cody.backend.storage.request.StorageRequest;
import com.cody.backend.storage.response.ProductResponse;
import com.cody.backend.storage.producer.ProductKafkaSender;
import com.cody.backend.storage.service.ProductStorageService;
import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.backend.storage.util.ValidRequestChecker;
import com.cody.common.core.MethodType;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.cody.domain.store.product.dto.ProductRequestDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/cody/v1/product/storage")
@RequiredArgsConstructor
public class ProductStorageController {
    private final ProductKafkaSender productKafkaSender;
    private final ProductStorageService productStorageService;
    private final ValidRequestChecker validRequestChecker;

    @PostMapping(value = "/insert")
    public ProductResponse insertProducts(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<ProductRequestDTO> productRequestDTOS = DisplayProductConverter.convertToProductRequestDTO(MethodType.INSERT, products);
        List<ProductRequestDTO> insertedProducts = productStorageService.insertProducts(productRequestDTOS);
        List<DisplayProductRequest> insertedDisplayProducts = DisplayProductConverter.convertProductToDisplayProduct(products, insertedProducts, MethodType.INSERT);
        productKafkaSender.sendProducts(insertedDisplayProducts);
        return new ProductResponse(insertedDisplayProducts, products);
    }

    @DeleteMapping(value = "/delete")
    public ProductResponse deleteProducts(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<ProductRequestDTO> productRequestDTOS = DisplayProductConverter.convertToProductRequestDTO(MethodType.DELETE, products);
        List<ProductRequestDTO> deletedProducts = productStorageService.deleteProducts(productRequestDTOS);
        List<DisplayProductRequest> deletedDisplayProducts = DisplayProductConverter.convertProductToDisplayProduct(products, deletedProducts, MethodType.DELETE);
        productKafkaSender.sendProducts(deletedDisplayProducts);
        return new ProductResponse(deletedDisplayProducts, products);

    }
    @PutMapping(value = "/update")
    public ProductResponse updateProducts(@RequestBody StorageRequest request) {
        validRequestChecker.isNoneValid(request);
        List<DisplayProduct> products = request.getDisplayProducts();
        List<ProductRequestDTO> productRequestDTOS = DisplayProductConverter.convertToProductRequestDTO(MethodType.UPDATE, products);
        List<ProductRequestDTO> updatedProducts = productStorageService.updateProducts(productRequestDTOS);
        List<DisplayProductRequest> updatedDisplayProducts = DisplayProductConverter.convertProductToDisplayProduct(products, updatedProducts, MethodType.UPDATE);
        productKafkaSender.sendProducts(updatedDisplayProducts);
        return new ProductResponse(updatedDisplayProducts, products);
    }
}
