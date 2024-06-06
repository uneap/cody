package com.cody.backend.storage.service;

import com.cody.backend.storage.util.DisplayProductConverter;
import com.cody.backend.storage.util.UserConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.brand.dto.BrandRequest;
import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.cody.domain.store.seller.dto.SellerRequestDTO;
import com.cody.domain.store.user.dto.UserDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandFacadeService {
    private final BrandStorageService brandStorageService;
    private final UserStorageService userStorageService;
    private final SellerStorageService sellerStorageService;

    public List<BrandRequest> insertBrands(List<DisplayProduct> products) {
        List<BrandRequest> brandRequests = DisplayProductConverter.convertToBrandRequestDTO(MethodType.INSERT, products);
        return brandStorageService.insertBrands(brandRequests);
    }

    public List<AllUser> insertAllUser(List<BrandRequest> insertedBrands) {
        List<AllUser> allUsers = DisplayProductConverter.convertToAllUser(insertedBrands);
        List<UserDTO> insertedUsers = userStorageService.insertUsers(allUsers);
        List<SellerRequestDTO> insertedSellers = sellerStorageService.insertSellers(allUsers, insertedUsers);
        return UserConverter.convertToAllUser(insertedSellers, insertedUsers);
    }
    public List<BrandRequest> deleteBrands(List<DisplayProduct> products) {
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, products);
        return brandStorageService.deleteBrands(brands);
    }

    public List<AllUser> deleteAllUser(List<BrandRequest> insertedBrands) {
        List<AllUser> allUsers = DisplayProductConverter.convertToAllUser(insertedBrands);
        List<UserDTO> deletedUsers = userStorageService.deleteUsers(allUsers);
        List<SellerRequestDTO> deletedSellers = sellerStorageService.deleteSellers(allUsers, deletedUsers);
        return UserConverter.convertToAllUser(deletedSellers, deletedUsers);
    }

    public List<BrandRequest> updateBrands(List<DisplayProduct> products) {
        List<BrandRequest> brands = DisplayProductConverter.convertToBrandRequestDTO(MethodType.DELETE, products);
        return brandStorageService.updateBrands(brands);
    }
}
