package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllUser {
    private long userId;
    private Long adminId;
    private String adminName;
    private String userName;
    private LocalDateTime lastUpdatedDateTime;

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
