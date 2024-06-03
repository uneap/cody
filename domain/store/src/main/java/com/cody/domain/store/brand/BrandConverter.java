package com.cody.domain.store.brand;

import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.KafkaBrandDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandConverter {
    private final ObjectMapper mapper;
    public List<KafkaBrandDTO> convertUpdatedBrands(String payload) {
        try {
            return mapper.readValue(payload, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("[FAIL] Convert UpdatedTarget! message = {}", e.getMessage());
        }
        return null;
    }
    public void addMethodTypeAndBrands(Map<MethodType, List<KafkaBrandDTO>> methodTypeAndBrands, List<KafkaBrandDTO> kafkaBrands) {
        for (KafkaBrandDTO kafkaBrand : kafkaBrands) {
            MethodType methodType = kafkaBrand.getMethodType();
            List<KafkaBrandDTO> brands = methodTypeAndBrands.getOrDefault(methodType, new ArrayList<>());
            brands.add(kafkaBrand);
            methodTypeAndBrands.put(kafkaBrand.getMethodType(), brands);
        }
    }

    public List<BrandDTO> getSortedBrands(Map<MethodType, List<KafkaBrandDTO>> methodTypeAndBrands, MethodType type) {
        return methodTypeAndBrands.get(type).stream().sorted().collect(Collectors.toList());
    }
}
