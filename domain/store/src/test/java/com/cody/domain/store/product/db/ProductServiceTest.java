package com.cody.domain.store.product.db;

import com.cody.domain.store.TestConfiguration;
import com.cody.domain.store.product.dto.ProductDTO;
import com.cody.domain.store.product.dto.ProductRequestDTO;
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
class ProductServiceTest {
    @Autowired
    private ProductService productService;


    @Test
    void updateProducts_InvalidDataAccessApiUsage_FAIL() {
        Assertions.assertThrows(
            InvalidDataAccessApiUsageException.class, () -> productService.updateProducts(Arrays.asList(
            ProductRequestDTO.builder()
                             .brandId(-1L)
                             .categoryId(1L)
                             .price(10500L)
                             .name("product")
                             .build())));
    }

    @Test
    void updateProducts_EntityNotFound_FAIL() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.updateProducts(Arrays.asList(
            ProductRequestDTO.builder()
                             .id(-1L)
                             .brandId(-1L)
                             .categoryId(1L)
                             .price(10500L)
                             .name("product")
                             .build())));
    }

    @Test
    void updateProduct_SUCCESS() {
        ProductDTO productDTO = productService.updateProduct(ProductRequestDTO.builder()
                                                                              .id(3L)
                                                                              .brandId(2L)
                                                                              .categoryId(1L)
                                                                              .price(10500L)
                                                                              .name("productA")
                                                                              .build());
        Assertions.assertEquals("productA", productDTO.getName());
    }

    @Test
    void deleteProduct_FAIL() {
        Assertions.assertThrows(
            InvalidDataAccessApiUsageException.class,() -> productService.deleteProduct(
                ProductRequestDTO.builder().name("AA").build()));
    }

    @Test
    void deleteProduct_SUCCESS() {
        List<ProductDTO> products = productService.findAllById(List.of( 3L));
        Assertions.assertEquals(1, products.size());
        productService.deleteProduct(ProductRequestDTO.builder().id(3L).build());
        products = productService.findAllById(List.of(3L));
        Assertions.assertNotEquals(1, products.size());
    }

    @Test
    void insert_FAIL() {
        Assertions.assertThrows(
            DataIntegrityViolationException.class, () -> productService.insert(
                ProductRequestDTO.builder()
                                 .brandId(436436436L).categoryId(34353532L).name("product").price(242L).build()));
    }
    @Test
    void insert_SUCCESS() {
        ProductDTO productDTO = productService.insert(
            ProductRequestDTO.builder()
                             .brandId(2L)
                             .categoryId(1L)
                             .price(10500L)
                             .name("product")
                             .build());
        Assertions.assertEquals("product", productDTO.getName());
    }

    @Test
    void findAllById_SUCCESS() {
        List<ProductDTO> products = productService.findAllById(List.of(6L));
        Assertions.assertEquals(1, products.stream().filter(brand -> brand.getName().equals("product")).count());
    }

    @Test
    void findAll_FAIL() {
        List<ProductDTO> products = productService.findAllById(List.of(11464L, 126436L));
        Assertions.assertNotEquals(2, products.size());
    }
}