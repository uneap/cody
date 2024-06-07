package com.cody.backend.storage.response;

import com.cody.common.core.Response;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.cody.domain.store.product.dto.UniqueProduct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

@Getter
public class ProductResponse extends Response {

    private final Set<UniqueProduct> failedProducts;

    public ProductResponse(List<DisplayProductRequest> queriedProducts, List<DisplayProduct> request) {
        Set<UniqueProduct> queriedUniqueProducts = queriedProducts.stream()
                                                                  .map(product -> UniqueProduct
                                                                      .builder()
                                                                      .brandId(product.getProductId())
                                                                      .name(product.getProductName())
                                                                      .build())
                                                                  .collect(Collectors.toSet());
        failedProducts = request.stream()
                                .map(product -> UniqueProduct
                                    .builder()
                                    .brandId(product.getProductId())
                                    .name(product.getProductName())
                                    .build())
                                .filter(product -> !queriedUniqueProducts.contains(product))
                                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(failedProducts)) {
            this.statusCode = HttpStatus.OK.value();
            this.reason = HttpStatus.OK.getReasonPhrase();
        } else {
            this.statusCode = HttpStatus.EXPECTATION_FAILED.value();
            this.reason = HttpStatus.EXPECTATION_FAILED.getReasonPhrase();
        }
    }
}
