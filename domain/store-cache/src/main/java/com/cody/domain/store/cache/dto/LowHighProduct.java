package com.cody.domain.store.cache.dto;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class LowHighProduct {
   private DisplayProduct lowestProduct;
    private DisplayProduct highestProduct;
}
