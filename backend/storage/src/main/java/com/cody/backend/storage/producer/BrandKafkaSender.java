package com.cody.backend.storage.producer;

import com.cody.domain.store.brand.dto.BrandRequest;
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
public class BrandKafkaSender {
    @Value("${kafka.brand.topic}")
    private String topic;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async("senderExecutor")
    public void sendBrands(List<BrandRequest> products) throws IllegalStateException {
        try {
            String productsString = objectMapper.writeValueAsString(products);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, productsString);
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
