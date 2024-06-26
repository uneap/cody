package com.cody.domain.store.category.db;


import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity(name = "CATEGORY")
@Table(name = "CATEGORY", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "id")
})
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDAO {
    @Version
    @Column(name = "VER_NO", nullable = false)
    private Long version;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "CREATED_DATE", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @PrePersist
    public void onPrePersist() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        LocalDateTime parsedCreateDate = LocalDateTime.parse(customLocalDateTimeFormat, ISO_LOCAL_DATE_TIME);
        this.createdDate = parsedCreateDate;
        if(version == null) {
            this.version = 0L;
        }
        this.lastModifiedDate = parsedCreateDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        this.lastModifiedDate = LocalDateTime.parse(customLocalDateTimeFormat, ISO_LOCAL_DATE_TIME);
    }
}
