package com.cody.backend.full.cache.batch.steps;

import com.cody.domain.store.cache.dto.FullBrand;
import com.cody.domain.store.cache.service.redis.FullBrandService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FullBrandWriter implements ItemWriter<FullBrand> {
    private final FullBrandService fullBrandService;
    @Override
    public void write(@NonNull Chunk<? extends FullBrand> chunk) throws Exception {

        fullBrandService.addAll((List<FullBrand>)chunk.getItems());
    }
}
