package com.cody.domain.store.cache.dto;

import com.cody.common.core.MethodType;
import java.util.Map;
import lombok.Getter;

@Getter
public class DisplayProductRequest extends DisplayProduct {
    private final MethodType methodType;
    private AllUser allUser;
    public DisplayProductRequest(MethodType methodType) {
        super();
        this.methodType = methodType;
    }
    public DisplayProductRequest(MethodType methodType, Map<Long, AllUser> allUserMap) {
        super();
        this.methodType = methodType;
        this.allUser = allUserMap.get(getBrandId());
    }
}
