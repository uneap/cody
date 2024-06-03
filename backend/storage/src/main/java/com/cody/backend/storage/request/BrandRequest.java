package com.cody.backend.storage.request;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor
public class BrandRequest {

    @JsonProperty("brands")
    private List<BrandDTO> brands;

    public List<BrandRequestDTO> convertToKafkaVersion(MethodType methodType) {
        if (CollectionUtils.isEmpty(brands)) {
            return new ArrayList<>();
        }
        return brands.stream()
                     .map(brand -> BrandRequestDTO.builder()
                                                  .id(brand.getId() == null ? null : brand.getId())
                                                  .version(brand.getVersion())
                                                  .createdDate(brand.getCreatedDate())
                                                  .lastModifiedDate(brand.getLastModifiedDate())
                                                  .name(brand.getName())
                                                  .methodType(methodType)
                                                  .build())
                     .collect(Collectors.toList());
    }
}
