package com.cody.domain.store.brand.db;

import com.cody.domain.store.TestConfiguration;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.brand.dto.BrandRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
@ActiveProfiles({"store-local", "db-local"})
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @Test
    void insertAll_FAIL() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> brandService.insertAll(Arrays.asList(
            BrandRequestDTO.builder()
                           .id(1L)
                           .name("A").build(),
            BrandRequestDTO.builder().id(2L).name("B").build())));
    }

    @Test
    void insertAll_SUCCESS() {
        List<BrandDTO> brands = brandService.insertAll(Arrays.asList(BrandRequestDTO.builder()
                                                                                    .name("AA").build(), BrandRequestDTO.builder()
                                                                                 .name("BB")
                                                                                 .build()));
        Assertions.assertEquals(1, brands.stream().filter(brand -> brand.getName().equals("AA")).count());
        Assertions.assertEquals(1, brands.stream().filter(brand -> brand.getName().equals("BB")).count());
    }

    @Test
    void insert_SUCCESS() {
        BrandDTO brand = brandService.insert(BrandRequestDTO.builder().name("DD").build());
        Assertions.assertEquals("DD", brand.getName());
    }

    @Test
    void updateName_SUCCESS() {
        BrandDTO brandDAO = brandService.updateBrand(BrandRequestDTO.builder().name("CC").id(12L).build());
        Assertions.assertEquals("CC", brandDAO.getName());
    }

    @Test
    void updateName_FAIL() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> brandService.updateBrand(BrandRequestDTO.builder().name("HHHHHHH").id(-1L).build()));
    }

    @Test
    void findAllById_SUCCESS() {
        List<BrandDTO> brandDTOs = brandService.findAllById(List.of(1L, 2L));
        Assertions.assertEquals(1, brandDTOs.stream().filter(brand -> brand.getName().equals("A")).count());
        Assertions.assertEquals(1, brandDTOs.stream().filter(brand -> brand.getName().equals("B")).count());
    }
    @Test
    void findAllById_FAIL() {
        List<BrandDTO> brandDTOs = brandService.findAllById(List.of(11464L, 126436L));
        Assertions.assertNotEquals(2, brandDTOs.size());
    }

    @Test
    void findById_SUCCESS() {
        BrandDTO brandDTO = brandService.findById(3L);
        Assertions.assertEquals("C", brandDTO.getName());
    }

    @Test
    void deleteBrands_SUCCESS() {
        List<BrandDTO> brandDTOs = brandService.findAllById(List.of(12L, 13L));
        Assertions.assertEquals(2, brandDTOs.size());
        brandService.deleteBrands(Arrays.asList(BrandRequestDTO.builder().id(12L).build(), BrandRequestDTO.builder().id(13L).build()));
        brandDTOs = brandService.findAllById(List.of(12L, 13L));
        Assertions.assertNotEquals(2, brandDTOs.size());
    }

    @Test
    void deleteBrand_FAIL() {
        Assertions.assertThrows(
            InvalidDataAccessApiUsageException.class,() -> brandService.deleteBrand(BrandRequestDTO.builder().name("AA").build()));

    }
}