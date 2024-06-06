package com.cody.domain.store.seller.db;

import com.cody.domain.store.brand.db.BrandDAO;
import com.cody.domain.store.brand.db.BrandRepository;
import com.cody.domain.store.seller.dto.SellerDTO;
import com.cody.domain.store.user.db.UserDAO;
import com.cody.domain.store.user.db.UserRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public SellerDTO insert(SellerDTO seller) throws DataIntegrityViolationException {
        UserDAO userDAO = userRepository.getReferenceById(seller.getUserId());
        BrandDAO brandDAO = brandRepository.getReferenceById(seller.getBrandId());
        SellerDAO sellerDAO = new SellerDAO(seller, userDAO, brandDAO);
        sellerDAO = sellerRepository.save(sellerDAO);
        return SellerDTO.daoBuilder(sellerDAO);
    }

    @Transactional
    public void deleteUsers(List<SellerDTO> sellerDTOS) throws EmptyResultDataAccessException, IllegalArgumentException {
        List<Long> ids = sellerDTOS.stream().map(SellerDTO::getId).collect(Collectors.toList());
        sellerRepository.deleteAllById(ids);
    }

    @Transactional
    public void deleteUser(UserDTO userDTO) throws EmptyResultDataAccessException, InvalidDataAccessApiUsageException {
        sellerRepository.deleteById(userDTO.getId());
    }
    public List<SellerDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<SellerDAO> brands = sellerRepository.findAllById(ids);
        return brands.stream().map(SellerDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<SellerDTO> findAll() throws NoSuchElementException {
        List<SellerDAO> brands = sellerRepository.findAll();
        return brands.stream().map(SellerDTO::daoBuilder).collect(Collectors.toList());
    }

    public SellerDTO findById(long id) throws NoSuchElementException {
        Optional<SellerDAO> brandDAO = sellerRepository.findById(id);
        return brandDAO.map(SellerDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
