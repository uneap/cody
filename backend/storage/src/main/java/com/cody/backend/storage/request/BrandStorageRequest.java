package com.cody.backend.storage.request;

import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.cache.dto.AllUser;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class BrandStorageRequest {
    private List<BrandDTO> brands;
    private AllUser requestUser;
}
