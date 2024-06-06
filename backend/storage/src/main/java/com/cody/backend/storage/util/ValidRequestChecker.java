package com.cody.backend.storage.util;


import com.cody.backend.storage.request.StorageRequest;
import com.cody.domain.store.admin.db.AdminService;
import com.cody.domain.store.admin.dto.AdminDTO;
import com.cody.domain.store.cache.dto.AllUser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidRequestChecker {
    private final AdminService adminService;
    public void isNoneValid(StorageRequest storageRequest) {
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
    }
}
