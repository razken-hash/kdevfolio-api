
package com.api.kdevfolio.blog.services;

import com.api.kdevfolio.blog.dtos.CategoryResponseDto;
import com.api.kdevfolio.blog.dtos.CreateCategoryRequestDto;
import com.api.kdevfolio.blog.exceptions.CategoryNotFoundException;
import com.api.kdevfolio.blog.models.Category;
import com.api.kdevfolio.blog.repositories.CategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoriesRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID id) {
        Category category = findCategoryById(id);
        return toResponseDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryBySlug(String slug) {
        Category category = categoriesRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with slug: " + slug
                        )
                );

        return toResponseDto(category);
    }

    public CategoryResponseDto createCategory(
            CreateCategoryRequestDto request
    ) {
        Category category = new Category();
        category.setSlug(request.slug());
        category.setName(request.name());
        category.setDescription(request.description());

        Category savedCategory = categoriesRepository.save(category);

        return toResponseDto(savedCategory);
    }

    public CategoryResponseDto updateCategory(
            UUID id,
            CreateCategoryRequestDto request
    ) {
        Category category = findCategoryById(id);

        category.setSlug(request.slug());
        category.setName(request.name());
        category.setDescription(request.description());

        Category updatedCategory = categoriesRepository.save(category);

        return toResponseDto(updatedCategory);
    }

    public void deleteCategory(UUID id) {
        Category category = findCategoryById(id);
        categoriesRepository.delete(category);
    }

    private Category findCategoryById(UUID id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id: " + id
                        )
                );
    }

    private CategoryResponseDto toResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
