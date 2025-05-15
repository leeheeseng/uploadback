package com.clonecod.clone.controller;

import com.clonecod.clone.dto.TopCategoryDto;
import com.clonecod.clone.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<TopCategoryDto>> getAllCategories() {
        List<TopCategoryDto> categories = categoryService.getAllCategoriesWithSubcategories();
        return ResponseEntity.ok(categories);
    }
}