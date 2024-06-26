package com.cody.backend.storage.response;

import com.cody.common.core.Response;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

@Getter
public class BrandResponse extends Response {
    private final List<String> failedBrandNames;

    public BrandResponse(List<BrandRequest> queriedProducts, List<BrandDTO> request) {
        Set<String> brandNames = queriedProducts.stream()
                                                .map(BrandRequest::getName)
                                                .collect(Collectors.toSet());
        failedBrandNames = request.stream()
                                  .map(BrandDTO::getName)
                                  .filter(brandName -> !brandNames.contains(brandName))
                                  .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(failedBrandNames)) {
            this.statusCode = HttpStatus.OK.value();
            this.reason = HttpStatus.OK.getReasonPhrase();
        } else {
            this.statusCode = HttpStatus.EXPECTATION_FAILED.value();
            this.reason = HttpStatus.EXPECTATION_FAILED.getReasonPhrase();
        }
    }
}
