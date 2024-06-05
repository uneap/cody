package com.cody.backend.full.cache.batch.steps;

import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.cache.service.ProductStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisplayProductWriter implements ItemWriter<DisplayProduct> {
    private final ProductStorageService productStorageService;

    @Override
    public void write(Chunk<? extends DisplayProduct> chunk) throws Exception {
        for(DisplayProduct product : chunk) {
            productStorageService.addProductInCache(product);
        }
    }
}
