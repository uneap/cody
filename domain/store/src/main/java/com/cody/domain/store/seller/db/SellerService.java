package com.cody.domain.store.seller.db;

import com.cody.domain.store.seller.dto.UserDTO;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<UserDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<SellerDAO> brands = sellerRepository.findAllById(ids);
        return brands.stream().map(UserDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<UserDTO> findAll() throws NoSuchElementException {
        List<SellerDAO> brands = sellerRepository.findAll();
        return brands.stream().map(UserDTO::daoBuilder).collect(Collectors.toList());
    }

    public UserDTO findById(long id) throws NoSuchElementException {
        Optional<SellerDAO> brandDAO = sellerRepository.findById(id);
        return brandDAO.map(UserDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
