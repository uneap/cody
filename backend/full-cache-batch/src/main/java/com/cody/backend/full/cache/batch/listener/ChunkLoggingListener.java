package com.cody.backend.full.cache.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChunkLoggingListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {

    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("Chunk [{}][{}]", context.getStepContext().getStepExecution().getReadCount(), context.getStepContext().getStepExecution().getWriteCount());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.info("!!! Chunk Error !!! [{}]", context.toString());
        log.info("Chunk [{}][{}]", context.getStepContext().getJobName(), context.getStepContext().getStepName());
    }
}
