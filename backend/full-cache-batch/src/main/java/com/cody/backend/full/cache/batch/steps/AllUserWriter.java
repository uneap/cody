package com.cody.backend.full.cache.batch.steps;

import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.service.redis.AllUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AllUserWriter implements ItemWriter<AllUser> {
    private final AllUserService allUserService;
    @Override
    public void write(Chunk<? extends AllUser> chunk) throws Exception {
        allUserService.addUsers((List<AllUser>) chunk);
    }
}
