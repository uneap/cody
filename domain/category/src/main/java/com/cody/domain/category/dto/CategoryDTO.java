package com.cody.domain.category.dto;

import com.cody.domain.category.db.CategoryDAO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Long version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static CategoryDTO daoBuilder(CategoryDAO categoryDAO) {
        return builder()
            .id(categoryDAO.getId())
            .name(categoryDAO.getName())
            .createdDate(categoryDAO.getCreatedDate())
            .lastModifiedDate(categoryDAO.getLastModifiedDate())
            .version(categoryDAO.getVersion()).build();
    }
}
