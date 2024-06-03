package com.cody.domain.brand;

import com.cody.domain.store.brand.BrandService;
import com.cody.domain.store.brand.db.BrandDAO;
import com.cody.domain.store.brand.dto.BrandDTO;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.Test;

@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles({"brand-local", "db-local"})
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @Test
    void insertAll_FAIL() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> brandService.insertAll(Arrays.asList(
            BrandDTO.builder()
                    .id(11L)
                    .name("A").build(),
            BrandDTO.builder().id(11L).name("B").build())));
    }

    @Test
    void insertAll_SUCCESS() {
        List<BrandDAO> brands = brandService.insertAll(Arrays.asList(BrandDTO.builder()
                                                                             .name("A").build(), BrandDTO.builder()
                                                                                 .name("B")
                                                                                 .build()));
        Assertions.assertEquals(1, brands.stream().filter(brand -> brand.getName().equals("A")).count());
        Assertions.assertEquals(1, brands.stream().filter(brand -> brand.getName().equals("B")).count());
    }

    @Test
    void insert_SUCCESS() {
        BrandDAO brand = brandService.insert(BrandDTO.builder().name("D").build());
        Assertions.assertEquals("D", brand.getName());
    }

    @Test
    void updateName_SUCCESS() {
        BrandDAO brandDAO = brandService.updateBrand(BrandDTO.builder().name("HHHHHHH").id(12L).build());
        Assertions.assertEquals("HHHHHHH", brandDAO.getName());
    }

    @Test
    void updateName_FAIL() {
        BrandDAO brandDAO = brandService.updateBrand(BrandDTO.builder().name("HHHHHHH").id(18L).build());
        Assertions.assertThrows(NullPointerException.class, () -> "HHHHHHH".equals(brandDAO.getName()));
    }

    @Test
    void findAllById_SUCCESS() {
        List<BrandDTO> brandDTOs = brandService.findAllById(List.of(11L, 12L));
        Assertions.assertEquals(1, brandDTOs.stream().filter(brand -> brand.getName().equals("D")).count());
        Assertions.assertEquals(1, brandDTOs.stream().filter(brand -> brand.getName().equals("HHHHHHH")).count());
    }
    @Test
    void findAllById_FAIL() {
        List<BrandDTO> brandDTOs = brandService.findAllById(List.of(11464L, 126436L));
        Assertions.assertNotEquals(2, brandDTOs.size());
    }

    @Test
    void findById_SUCCESS() {
        BrandDTO brandDTO = brandService.findById(11L);
        Assertions.assertEquals("D", brandDTO.getName());
    }
}