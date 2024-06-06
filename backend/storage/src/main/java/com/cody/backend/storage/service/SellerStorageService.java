package com.cody.backend.storage.service;

import com.cody.backend.storage.util.UserConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.seller.db.SellerService;
import com.cody.domain.store.seller.dto.SellerDTO;
import com.cody.domain.store.seller.dto.SellerRequestDTO;
import com.cody.domain.store.user.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerStorageService {
    private final SellerService sellerService;
    public List<SellerRequestDTO> insertSellers(List<AllUser> allUsers, List<UserDTO> insertedUsers) {
        List<SellerDTO> insertedSellers = new ArrayList<>();
        List<SellerDTO> sellerDTOS = UserConverter.convertALlUserToSellerDTO(allUsers, insertedUsers);
        for(SellerDTO sellerDTO : sellerDTOS){
            SellerDTO insertedSeller = sellerService.insert(sellerDTO);
            insertedSellers.add(insertedSeller);
        }
        return insertedSellers.stream()
                              .filter(Objects::nonNull)
                              .map(seller -> SellerRequestDTO.dtoBuilder(seller, MethodType.INSERT))
                              .collect(Collectors.toList());
    }

    public List<SellerRequestDTO> deleteSellers(List<AllUser> allUsers, List<UserDTO> deletedUsers) {
        List<SellerDTO> sellerDTOS = UserConverter.convertALlUserToSellerDTO(allUsers, deletedUsers);
        sellerService.deleteUsers(sellerDTOS);
        return sellerDTOS.stream()
                    .map(brand -> SellerRequestDTO.dtoBuilder(brand, MethodType.INSERT))
                    .collect(Collectors.toList());
    }
}
