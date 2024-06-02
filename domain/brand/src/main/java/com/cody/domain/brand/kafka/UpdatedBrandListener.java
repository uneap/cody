package com.cody.domain.brand.kafka;

import static com.cody.common.core.Constants.BATCH_SIZE;
import static org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE;

import com.cody.common.core.MethodType;
import com.cody.domain.brand.BrandConverter;
import com.cody.domain.brand.BrandService;
import com.cody.domain.brand.dto.BrandDTO;
import com.cody.domain.brand.dto.KafkaBrandDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Value("${kafka.retry.topic}")
    private String retryTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BrandService brandService;
    private final BrandConverter brandConverter;
    private final Map<MethodType, List<KafkaBrandDTO>> brands = new HashMap<>();

    @RetryableTopic(
        backoff = @Backoff(delay = 10 * 1000, multiplier = 3, maxDelay = 10 * 60 * 1000),
        topicSuffixingStrategy = SUFFIX_WITH_INDEX_VALUE,
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
        listenerContainerFactory = "kafkaListenerContainerFactory"
    )
    @KafkaListener(id = "updated-brand-stream", topics = {"${kafka.topic}", "{kafka.retry.topic}"})
    public void consume(@Payload String payload,
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long receivedTimestamp,
        @Header(KafkaHeaders.GROUP_ID) String groupId,
        Acknowledgment ack) {
        log.info("[LISTEN] partitionId : {}, offset : {}, groupId : {}, receivedTimestamp : {}, payload : {}",
            partitionId, offset, groupId, receivedTimestamp, payload);
        try {
            List<KafkaBrandDTO> brandDTOs = brandConverter.convertUpdatedBrands(payload);
            if (CollectionUtils.isEmpty(brands)) {
                return;
            }
            brandConverter.addMethodTypeAndBrands(brands, brandDTOs);
            if(brands.get(MethodType.UPDATE).size() >= BATCH_SIZE) {
                List<BrandDTO> brandsToUpdate = brandConverter.getSortedBrands(brands, MethodType.UPDATE);
                brandService.updateBrands(brandsToUpdate);
            }
            if(brands.get(MethodType.DELETE).size() >= BATCH_SIZE) {
                List<BrandDTO> brandsToDelete = brandConverter.getSortedBrands(brands, MethodType.DELETE);
                brandService.deleteBrands(brandsToDelete);
            }
            if(brands.get(MethodType.INSERT).size() >= BATCH_SIZE) {
                List<BrandDTO> brandsToInsert = brandConverter.getSortedBrands(brands, MethodType.INSERT);
                brandService.deleteBrands(brandsToInsert);
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
