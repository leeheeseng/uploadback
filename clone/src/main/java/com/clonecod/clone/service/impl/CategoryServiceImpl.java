package com.clonecod.clone.service.impl;

import com.clonecod.clone.dto.TopCategoryDto;
import com.clonecod.clone.repository.TopCategoryRepository;
import com.clonecod.clone.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final TopCategoryRepository topCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TopCategoryDto> getAllCategoriesWithSubcategories() {
        return topCategoryRepository.findAllWithSubCategories().stream()
                .map(TopCategoryDto::fromEntity)
                .collect(Collectors.toList());
    }
}