
package com.api.kdevfolio.blog.controllers;

import com.api.kdevfolio.blog.dtos.CategoryResponseDto;
import com.api.kdevfolio.blog.dtos.CreateCategoryRequestDto;
import com.api.kdevfolio.blog.services.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blog/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable UUID id) {
        return categoriesService.getCategoryById(id);
    }

    @GetMapping("/slug/{slug}")
    public CategoryResponseDto getCategoryBySlug(
            @PathVariable String slug
    ) {
        return categoriesService.getCategoryBySlug(slug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CreateCategoryRequestDto request
    ) {
        return categoriesService.createCategory(request);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCategoryRequestDto request
    ) {
        return categoriesService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoriesService.deleteCategory(id);
    }
}
