package com.cody.backend.storage.service;

import com.cody.backend.storage.util.UserConverter;
import com.cody.common.core.MethodType;
import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.user.db.UserService;
import com.cody.domain.store.user.dto.UserDTO;
import com.cody.domain.store.user.dto.UserRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStorageService {
    private final UserService userService;
    public List<UserDTO> insertUsers(List<AllUser> allUsers) {
        List<UserDTO> users = UserConverter.convertAllUserToUserDTO(allUsers);
        List<UserDTO> insertedUsers = userService.insertAll(users);
        return insertedUsers.stream()
                     .map(brand -> UserRequestDTO.dtoBuilder(brand, MethodType.INSERT))
                     .collect(Collectors.toList());
    }

    public List<UserDTO> deleteUsers(List<AllUser> allUsers) {
        List<UserDTO> users = UserConverter.convertAllUserToUserDTO(allUsers);
        userService.deleteUsers(users);
        return users.stream()
                    .map(brand -> UserRequestDTO.dtoBuilder(brand, MethodType.INSERT))
                    .collect(Collectors.toList());
    }
}
