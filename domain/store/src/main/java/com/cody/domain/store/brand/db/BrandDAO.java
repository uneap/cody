package com.cody.domain.store.brand.db;


import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.product.db.ProductDAO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity(name = "BRAND")
@Table(name = "BRAND", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "id")
})
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

    @OneToMany(mappedBy = "brand", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductDAO> products;
    @PrePersist
    public void onPrePersist() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        LocalDateTime parsedCreateDate = LocalDateTime.parse(customLocalDateTimeFormat, ISO_LOCAL_DATE_TIME);
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
        String customLocalDateTimeFormat = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        this.lastModifiedDate = LocalDateTime.parse(customLocalDateTimeFormat, ISO_LOCAL_DATE_TIME);
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
