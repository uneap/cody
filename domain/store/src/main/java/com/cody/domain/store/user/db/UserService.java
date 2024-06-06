package com.cody.domain.store.user.db;

import com.cody.domain.store.user.dto.UserDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public List<UserDTO> insertAll(List<UserDTO> brands) throws DataIntegrityViolationException {
        List<UserDAO> userDAOs = brands.stream().map(UserDAO::new).toList();
        userDAOs = userRepository.saveAll(userDAOs);
        if(!CollectionUtils.isEmpty(userDAOs)) {
            return userDAOs.stream().map(UserDTO::daoBuilder).collect(Collectors.toList());
        }
        throw new DataIntegrityViolationException("insert fail");
    }

    @Transactional
    public UserDTO insert(UserDTO user) throws DataIntegrityViolationException {
        UserDAO userDAO = new UserDAO(user);
        userDAO = userRepository.save(userDAO);
        return UserDTO.daoBuilder(userDAO);
    }

    @Transactional
    public void deleteUsers(List<UserDTO> userDTOS) throws EmptyResultDataAccessException, IllegalArgumentException {
        List<Long> ids = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toList());
        userRepository.deleteAllById(ids);
    }

    @Transactional
    public void deleteUser(UserDTO userDTO) throws EmptyResultDataAccessException, InvalidDataAccessApiUsageException {
        userRepository.deleteById(userDTO.getId());
    }

    public List<UserDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<UserDAO> brands = userRepository.findAllById(ids);
        return brands.stream().map(UserDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<UserDTO> findAll() throws NoSuchElementException {
        List<UserDAO> brands = userRepository.findAll();
        return brands.stream().map(UserDTO::daoBuilder).collect(Collectors.toList());
    }

    public UserDTO findById(long id) throws NoSuchElementException {
        Optional<UserDAO> brandDAO = userRepository.findById(id);
        return brandDAO.map(UserDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
