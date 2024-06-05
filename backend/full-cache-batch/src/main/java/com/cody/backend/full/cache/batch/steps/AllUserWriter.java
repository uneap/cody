package com.cody.backend.full.cache.batch.steps;

import com.cody.domain.store.cache.dto.DisplayProduct;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class AllUserWriter implements ItemWriter<DisplayProduct> {

    @Override
    public void write(Chunk<? extends DisplayProduct> chunk) throws Exception {

    }
}
