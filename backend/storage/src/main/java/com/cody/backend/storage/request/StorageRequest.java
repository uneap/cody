package com.cody.backend.storage.request;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StorageRequest {

    @JsonProperty("products")
    private List<DisplayProduct> displayProducts;
}
