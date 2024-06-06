package com.cody.domain.store.seller.db;

import com.cody.domain.store.TestConfiguration;
import com.cody.domain.store.seller.dto.SellerDTO;
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
class SellerServiceTest {
    @Autowired
    private SellerService sellerService;
    @Test
    void findAllById_SUCCESS() {
        List<SellerDTO> sellerDTOList = sellerService.findAllById(Arrays.asList(1L));
        Assertions.assertEquals(1, sellerDTOList.size());
    }
    @Test
    void findAllById_FAIL() {
        List<SellerDTO> sellerDTOList = sellerService.findAllById(Arrays.asList(11L));
        Assertions.assertNotEquals(2, sellerDTOList.size());
    }
}