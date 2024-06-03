package com.cody.domain.store.category.db;

import com.cody.domain.store.category.dto.CategoryDTO;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAllById(List<Long> ids) throws NoSuchElementException {
        List<CategoryDAO> brands = categoryRepository.findAllById(ids);
        return brands.stream().map(CategoryDTO::daoBuilder).collect(Collectors.toList());
    }

    public List<CategoryDTO> findAll() throws NoSuchElementException {
        List<CategoryDAO> brands = categoryRepository.findAll();
        return brands.stream().map(CategoryDTO::daoBuilder).collect(Collectors.toList());
    }

    public CategoryDTO findById(long id) throws NoSuchElementException {
        Optional<CategoryDAO> brandDAO = categoryRepository.findById(id);
        return brandDAO.map(CategoryDTO::daoBuilder).orElseThrow(NoSuchElementException::new);
    }
}
