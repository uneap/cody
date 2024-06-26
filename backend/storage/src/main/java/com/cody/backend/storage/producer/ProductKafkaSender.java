package com.cody.backend.storage.producer;

import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductKafkaSender {
    @Value("${kafka.product.topic}")
    private String topic;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async("senderExecutor")
    public void sendProducts(List<DisplayProductRequest> brands) throws IllegalStateException {
        try {
            String brandsString = objectMapper.writeValueAsString(brands);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, brandsString);
            future.whenComplete((result, ex) -> {
                if(ex != null) {
                    log.error("kafka send error : {}", ex.toString());
                    throw new IllegalStateException(ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("json processing error : {}", e.toString());
        }
    }
}
