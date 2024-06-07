package com.cody.backend.storage.request;

import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StorageRequest {
    private List<DisplayProduct> displayProducts;
    private AllUser requestUser;
}
