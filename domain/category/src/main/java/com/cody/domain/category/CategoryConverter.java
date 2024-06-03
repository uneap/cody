package com.cody.domain.category;

import com.cody.common.core.MethodType;
import com.cody.domain.category.dto.CategoryRequestDTO;
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
public class CategoryConverter {
    private final ObjectMapper objectMapper;
    public List<CategoryRequestDTO> convertUpdatedBrands(String payload) {
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("[FAIL] Convert UpdatedBrands! message = {}", e.getMessage());
        }
        return null;
    }
    public void addMethodTypeAndBrands(Map<MethodType, List<CategoryRequestDTO>> methodTypeAndBrands, List<CategoryRequestDTO> kafkaBrands) {
        for (CategoryRequestDTO kafkaBrand : kafkaBrands) {
            MethodType methodType = kafkaBrand.getMethodType();
            List<CategoryRequestDTO> brands = methodTypeAndBrands.getOrDefault(methodType, new ArrayList<>());
            brands.add(kafkaBrand);
            methodTypeAndBrands.put(kafkaBrand.getMethodType(), brands);
        }
    }

    public List<CategoryRequestDTO> getSortedBrands(Map<MethodType, List<CategoryRequestDTO>> methodTypeAndBrands, MethodType type) {
        return methodTypeAndBrands.get(type).stream().sorted().collect(Collectors.toList());
    }
}
