package com.cody.backend.cache.kafka.consumer;

import static org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.BrandConverter;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.FullBrand;
import com.cody.domain.store.cache.service.RefreshProductService;
import com.cody.domain.store.cache.service.redis.FullBrandService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatedBrandListener {
    @Value("${kafka.brand.retry.topic}")
    private String retryTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BrandConverter brandConverter;
    private final RefreshProductService refreshProductService;
    private final FullBrandService fullBrandService;

    @RetryableTopic(
        backoff = @Backoff(delay = 10 * 1000, multiplier = 3, maxDelay = 10 * 60 * 1000),
        autoCreateTopics = "false",
        topicSuffixingStrategy = SUFFIX_WITH_INDEX_VALUE,
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
        listenerContainerFactory = "kafkaListenerContainerFactory"
    )
    @KafkaListener(id = "updated-brand-stream", topics = {"${kafka.brand.topic}", "${kafka.brand.retry.topic}"})
    public void consume(@Payload String payload,
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long receivedTimestamp,
        @Header(KafkaHeaders.GROUP_ID) String groupId,
        Acknowledgment ack) {
        log.info("[LISTEN] partitionId : {}, offset : {}, groupId : {}, receivedTimestamp : {}, payload : {}",
            partitionId, offset, groupId, receivedTimestamp, payload);
        try {
            List<BrandRequest> displayProducts = brandConverter.convertUpdatedBrands(payload);
            if (CollectionUtils.isEmpty(displayProducts)) {
                ack.acknowledge();
                log.error("[EXCEPTION] brand is null");
                return;
            }
            MethodType type = displayProducts.get(0).getMethodType();
            List<FullBrand> brands = displayProducts.stream()
                                                    .filter(Objects::nonNull)
                                                    .filter(BrandRequest::isConsumeValid)
                                                    .map(brandRequest -> FullBrand.builder()
                                                                                             .id(brandRequest.getId())
                                                                                             .name(brandRequest.getName())
                                                                                             .lastUpdatedTime(brandRequest.getLastModifiedDate())
                                                                                             .build())
                                                    .distinct()
                                                    .collect(Collectors.toList());
            if(type == MethodType.INSERT) {
                fullBrandService.addAll(brands);
            } if(type == MethodType.DELETE) {
                fullBrandService.removeAll(brands.stream().map(FullBrand::getId).collect(Collectors.toList()));
            } if(type == MethodType.UPDATE) {
                fullBrandService.updateAll(brands);
            }
            for (BrandRequest brandRequest : displayProducts){
                if(brandRequest == null || !brandRequest.isConsumeValid()) {
                    log.error("[EXCEPTION] brand input is invalid");
                    continue;
                }
                if(type == MethodType.DELETE) {
                    refreshProductService.deleteBrandInCache(brandRequest.getId());
                }
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[EXCEPTION] messgage : {}", e.toString());
            throw new UnknownError(e.getMessage());
        }
    }

    @DltHandler
    public void dltHandler(ConsumerRecord<String, String> record,
        @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment acknowledgment,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage) {
        try {
            log.error("received message='{}' with partitionId='{}', offset='{}', topic='{}', errorMessage='{}'",
                record.value(), offset, partitionId, topic, errorMessage);
            kafkaTemplate.send(retryTopic, record.value());
            if (Objects.nonNull(acknowledgment)) {
                acknowledgment.acknowledge();
            }
        } catch(Exception e) {
            log.error("[Fail to dead letter topic]: received message='{}' with partitionId='{}', offset='{}', topic='{}', errorMessage='{}'",
                record.value(), offset, partitionId, topic, errorMessage);
        }
    }
}
