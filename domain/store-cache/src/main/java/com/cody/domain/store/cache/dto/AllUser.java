package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllUser {
    private final long userId;
    private final Long adminId;
    private final String adminName;
    private final String userName;
    private final LocalDateTime lastUpdatedDateTime;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AllUser user) {
            return this.userId == user.userId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
