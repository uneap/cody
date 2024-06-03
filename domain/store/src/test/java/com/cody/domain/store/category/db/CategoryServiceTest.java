package com.cody.domain.store.category.db;

import com.cody.domain.store.TestConfiguration;
import com.cody.domain.store.category.dto.CategoryDTO;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles({"store-local", "db-local"})
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    void findAllById_SUCCESS() {
        List<CategoryDTO> categoryDTOList = categoryService.findAllById(Arrays.asList(1L,2L));
        Assertions.assertEquals(2, categoryDTOList.size());
    }
    @Test
    void findAllById_FAIL() {
        List<CategoryDTO> categoryDTOList = categoryService.findAllById(Arrays.asList(11L,22L));
        Assertions.assertNotEquals(2, categoryDTOList.size());
    }
}