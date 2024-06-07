package com.cody.backend.storage.util;


import com.cody.backend.storage.request.BrandStorageRequest;
import com.cody.backend.storage.request.StorageRequest;
import com.cody.common.core.MethodType;
import com.cody.domain.store.admin.db.AdminService;
import com.cody.domain.store.admin.dto.AdminDTO;
import com.cody.domain.store.brand.dto.BrandDTO;
import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ValidRequestChecker {
    private final AdminService adminService;
    public void isNoneValidByProduct(StorageRequest storageRequest, MethodType type) throws NoSuchElementException {
        if(storageRequest == null) {
            throw new InvalidDataAccessApiUsageException("EMPTY REQUEST");
        }
        AllUser user = storageRequest.getRequestUser();
        if(user == null || user.getAdminId() == null) {
            throw new InvalidRequestException("IS NOT ADMIN");
        }
        AdminDTO admin = adminService.findById(user.getUserId());
        if(admin == null) {
            throw new InvalidRequestException("IS NOT ADMIN");
        }
        List<DisplayProduct> products = storageRequest.getDisplayProducts();
        if(CollectionUtils.isEmpty(products)) {
            throw new InvalidDataAccessApiUsageException("EMPTY REQUEST");
        }
        if(MethodType.INSERT == type) {
            if(products.stream().filter(DisplayProduct::isInsertValid).count() != products.size()) {
                throw new InvalidRequestException("IS INVALID PRODUCT DATA");
            }
        } else {
            if(products.stream().filter(DisplayProduct::isValid).count() != products.size()) {
                throw new InvalidRequestException("IS INVALID PRODUCT DATA");
            }
        }
    }

    public void isNoneValidByBrand(BrandStorageRequest brandStorageRequest, MethodType type) {
        if(brandStorageRequest == null) {
            throw new InvalidDataAccessApiUsageException("EMPTY REQUEST");
        }
        AllUser user = brandStorageRequest.getRequestUser();
        if(user == null || user.getAdminId() == null) {
            throw new InvalidRequestException("IS NOT ADMIN");
        }
        AdminDTO admin = adminService.findById(user.getUserId());
        if(admin == null) {
            throw new InvalidRequestException("IS NOT ADMIN");
        }
        List<BrandDTO> products = brandStorageRequest.getBrands();
        if(CollectionUtils.isEmpty(products)) {
            throw new InvalidDataAccessApiUsageException("EMPTY REQUEST");
        }
        if(MethodType.INSERT == type) {
            if(products.stream().filter(BrandDTO::isInsertValid).count() != products.size()) {
                throw new InvalidRequestException("IS INVALID PRODUCT DATA");
            }
        } else {
            if(products.stream().filter(BrandDTO::isValid).count() != products.size()) {
                throw new InvalidRequestException("IS INVALID PRODUCT DATA");
            }
        }

    }
}
