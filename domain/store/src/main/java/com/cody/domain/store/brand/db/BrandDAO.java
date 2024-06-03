package com.cody.domain.store.brand.db;


import com.cody.domain.store.brand.dto.BrandDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity(name = "BRAND")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandDAO {
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
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime parsedCreateDate = LocalDateTime.parse(customLocalDateTimeFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.createdDate = parsedCreateDate;
        if(version == null) {
            this.version = 0L;
        } else {
            this.version = this.version + 1;
        }
        this.lastModifiedDate = parsedCreateDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModifiedDate = LocalDateTime.parse(customLocalDateTimeFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public BrandDAO(BrandDTO brand) {
        if(brand.getId() != null) {
            this.id = brand.getId();
        }
        this.name = brand.getName();
    }

    public void changeData(BrandDTO brandDTO) {
        // TODO: 만일 name 외에 변경될 수 있는 데이터 추가 될 경우, 조건 추가
        if(this.name.equals(brandDTO.getName())){
           return;
        }
        this.name = brandDTO.getName();
        this.version = this.version + 1;
    }
}
