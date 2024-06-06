package com.cody.domain.store.admin.db;


import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.domain.store.admin.dto.AdminDTO;
import com.cody.domain.store.user.db.UserDAO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity(name = "ADMIN")
@Table(name = "ADMIN", uniqueConstraints = {
    @UniqueConstraint(columnNames = "NAME"),
    @UniqueConstraint(columnNames = "ID"),
    @UniqueConstraint(columnNames = { "ID", "USER_ID" })
})
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDAO {
    @Version
    @Column(name = "VER_NO", nullable = false)
    private Long version;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "CREATED_DATE", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "LAST_MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", updatable = false, insertable = true)
    private UserDAO user;

    @PrePersist
    public void onPrePersist() {
        this.createdDate = LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        if(version == null) {
            this.version = 0L;
        }
        this.lastModifiedDate = LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
    }

    @PreUpdate
    public void onPreUpdate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
        this.lastModifiedDate = LocalDateTime.parse(customLocalDateTimeFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public AdminDAO(AdminDTO adminDTO, UserDAO user) {
        if(adminDTO.getId() != null) {
            this.id = adminDTO.getId();
        }
        this.user = user;
    }
}
