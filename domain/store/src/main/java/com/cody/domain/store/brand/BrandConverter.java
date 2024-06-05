package com.cody.domain.store.brand;

import com.cody.domain.store.cache.dto.DisplayProductRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandConverter {
    private final ObjectMapper objectMapper;
    public List<DisplayProductRequest> convertUpdatedBrands(String payload) {
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("[FAIL] Convert UpdatedBrands! message = {}", e.getMessage());
        }
        return null;
    }
}
