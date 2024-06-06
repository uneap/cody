package com.cody.domain.store.user.db;


import com.cody.domain.store.user.dto.UserDTO;
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
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity(name = "USERS")
@Table(name = "USERS", uniqueConstraints = {
    @UniqueConstraint(columnNames = "id")
})
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDAO {
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
        }
        this.lastModifiedDate = parsedCreateDate;
    }

    public UserDAO(UserDTO user) {
        if(user.getId() != null) {
            this.id = user.getId();
        }
        this.name = user.getName();
    }

    @PreUpdate
    public void onPreUpdate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModifiedDate = LocalDateTime.parse(customLocalDateTimeFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
