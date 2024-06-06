package com.cody.backend.storage.util;

import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.seller.dto.SellerDTO;
import com.cody.domain.store.seller.dto.SellerRequestDTO;
import com.cody.domain.store.user.dto.UserDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserConverter {

    public static List<AllUser> convertToAllUser(List<SellerRequestDTO> insertedSellers,
        List<UserDTO> insertedUsers) {
        Map<Long, UserDTO> userMap = insertedUsers.stream().collect(
            Collectors.toMap(UserDTO::getId, Function.identity()));
        return insertedSellers.stream()
                              .filter(sellerDTO -> userMap.containsKey(sellerDTO.getUserId()))
                              .map(sellerDTO -> AllUser.builder()
                                                       .userName(userMap.get(sellerDTO.getUserId()).getName())
                                                       .userId(sellerDTO.getUserId())
                                                       .brandId(sellerDTO.getBrandId())
                                                       .sellerName(sellerDTO.getName())
                                                       .lastUpdatedDateTime(sellerDTO.getLastModifiedDate())
                                                       .sellerId(sellerDTO.getId())
                                                       .build()).collect(Collectors.toList());
    }

    public static List<UserDTO> convertAllUserToUserDTO(List<AllUser> allUsers) {
        return allUsers.stream().map(allUser -> UserDTO.builder()
                                                       .id(allUser.getUserId())
                                                       .name(allUser.getUserName())
                                                       .build())
                       .collect(Collectors.toList());
    }
    public static List<SellerDTO> convertALlUserToSellerDTO(List<AllUser> allUsers, List<UserDTO> queriedUser) {
        Set<Long> userIds = queriedUser.stream().map(UserDTO::getId).collect(Collectors.toSet());
        return allUsers.stream()
                       .filter(user -> userIds.contains(user.getUserId()))
                       .map(allUser -> SellerDTO.builder()
                                                .userId(allUser.getUserId())
                                                .id(allUser.getSellerId())
                                                .name(allUser.getSellerName())
                                                .brandId(allUser.getBrandId())
                                                .build())
                       .collect(Collectors.toList());
    }
}
