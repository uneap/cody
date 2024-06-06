package com.cody.domain.store.admin.db;

import com.cody.domain.store.admin.dto.AdminDTO;
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
public class AdminService {
    private final AdminRepository adminRepository;

    public List<AdminDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<AdminDAO> brands = adminRepository.findAllById(ids);
        return brands.stream().map(AdminDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<AdminDTO> findAll() throws NoSuchElementException {
        List<AdminDAO> brands = adminRepository.findAll();
        return brands.stream().map(AdminDTO::daoBuilder).collect(Collectors.toList());
    }

    public AdminDTO findById(long id) throws NoSuchElementException {
        Optional<AdminDAO> brandDAO = adminRepository.findById(id);
        return brandDAO.map(AdminDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
