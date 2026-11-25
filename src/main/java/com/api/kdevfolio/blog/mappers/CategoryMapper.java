package com.api.kdevfolio.blog.mappers;

import com.api.kdevfolio.blog.dtos.CategoryRequest;
import com.api.kdevfolio.blog.dtos.CategoryResponse;
import com.api.kdevfolio.blog.models.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static void apply(CategoryRequest request, Category category) {
        category.setSlug(request.slug());
        category.setName(request.name());
        category.setDescription(request.description());
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
