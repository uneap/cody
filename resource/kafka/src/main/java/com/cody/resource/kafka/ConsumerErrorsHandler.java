package com.cody.resource.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerErrorsHandler {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void postProcessDltMessage(ConsumerRecord<String, String> record,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
        @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.error("[DLT Log] received message='{}' with partitionId='{}', offset='{}', topic='{}', groupId='{}', errorMessage ='{}'",
            record.value(), partitionId, offset, topic, groupId, errorMessage);
        kafkaTemplate.send(topic, record.value());
        //TODO: 실패했음을 알 수 있도록 알림 추가
    }
}
