package com.cody.domain.store.cache.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class FullBrand {

    private String name;
    private long id;
    private LocalDateTime lastUpdatedTime;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FullBrand brand) {
            return this.id == brand.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
