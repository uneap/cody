package com.cody.domain.store.product;

import com.cody.common.core.MethodType;
import com.cody.domain.store.product.dto.ProductRequestDTO;
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
public class ProductConverter {
    private final ObjectMapper objectMapper;
    public List<ProductRequestDTO> convertUpdatedProducts(String payload) {
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("[FAIL] Convert UpdatedProduct! message = {}", e.getMessage());
        }
        return null;
    }
    public void addMethodTypeAndProducts(Map<MethodType, List<ProductRequestDTO>> methodTypeAndProducts, List<ProductRequestDTO> products) {
        for (ProductRequestDTO product : products) {
            MethodType methodType = product.getMethodType();
            List<ProductRequestDTO> brands = methodTypeAndProducts.getOrDefault(methodType, new ArrayList<>());
            brands.add(product);
            methodTypeAndProducts.put(product.getMethodType(), brands);
        }
    }

    public List<ProductRequestDTO> getSortedProducts(Map<MethodType, List<ProductRequestDTO>> methodTypeAndProducts, MethodType type) {
        return methodTypeAndProducts.get(type).stream().sorted().collect(Collectors.toList());
    }
}
