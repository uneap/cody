package com.cody.domain.store.product.db;


import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.brand.db.BrandDAO;
import com.cody.domain.store.category.db.CategoryDAO;
import com.cody.domain.store.product.dto.ProductDTO;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Table(name = "PRODUCT", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "name", "brand" }),
    @UniqueConstraint(columnNames = "id")
})
@Entity(name = "PRODUCT")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDAO {
    @Version
    @Column(name = "VER_NO", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CategoryDAO category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID", referencedColumnName = "ID", updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private BrandDAO brand;

    @Column
    private long price;

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
        LocalDateTime parsedCreateDate = LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        this.createdDate = parsedCreateDate;
        if(version == null) {
            this.version = 0L;
        }
        this.lastModifiedDate = parsedCreateDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        LocalDateTime parsedCreateDate = LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        this.lastModifiedDate = parsedCreateDate;
    }

    public ProductDAO(@NonNull ProductDTO product, CategoryDAO category, BrandDAO brand) {
        if(product.getId() != null) {
            this.id = product.getId();
        }
        this.category = category;
        this.brand = brand;
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public void changeData(ProductDTO product) {
        // TODO: 만일 name, price 외에 변경될 수 있는 데이터 추가 될 경우, 조건 추가
        if(this.name.equals(product.getName()) && this.price == product.getPrice()){
            return;
        }
        this.price = product.getPrice();
        this.name = product.getName();
        this.version = this.version + 1;
    }
}
