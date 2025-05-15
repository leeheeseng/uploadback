package com.clonecod.clone.service;

import com.clonecod.clone.dto.TopCategoryDto;
import java.util.List;

public interface CategoryService {
    List<TopCategoryDto> getAllCategoriesWithSubcategories();
}