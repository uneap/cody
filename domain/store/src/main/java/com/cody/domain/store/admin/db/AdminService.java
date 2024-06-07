package com.cody.domain.store.admin.db;

import com.cody.domain.store.admin.dto.AdminDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<AdminDTO> findAllById(List<Long> ids) throws InvalidRequestException {
        List<AdminDAO> brands = adminRepository.findAllById(ids);
        return brands.stream().map(AdminDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<AdminDTO> findAll() throws InvalidRequestException {
        List<AdminDAO> brands = adminRepository.findAll();
        return brands.stream().map(AdminDTO::daoBuilder).collect(Collectors.toList());
    }

    public AdminDTO findById(long id) throws InvalidRequestException {
        Optional<AdminDAO> brandDAO = adminRepository.findById(id);
        return brandDAO.map(AdminDTO::daoBuilder).orElseThrow(() -> new InvalidRequestException("IS NOT ADMIN"));
    }
}
//{
//    "displayProducts": [{
//        "brandName": "J"
//    }],
//    "requestUser": {
//        "userId":35325,
//        "adminId":34
//    }
//}
//{
//    "displayProducts": [{
//        "brandName": "J"
//    }],
//    "requestUser": {
//        "userId":1,
//        "adminId":1
//    }
//}