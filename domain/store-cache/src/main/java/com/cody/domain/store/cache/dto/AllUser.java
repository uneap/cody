package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllUser {
    private final long userId;
    private final long sellerId;
    private final String sellerName;
    private final String userName;
    private final LocalDateTime lastUpdatedDateTime;

}
