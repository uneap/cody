package com.cody.domain.store.cache.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class LowHighProduct {
   private DisplayProduct lowestProduct;
    private DisplayProduct highestProduct;
}
