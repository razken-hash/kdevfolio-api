package com.api.kdevfolio.blog.services;

import com.api.kdevfolio.blog.dtos.CategoryRequest;
import com.api.kdevfolio.blog.dtos.CategoryResponse;
import com.api.kdevfolio.blog.mappers.CategoryMapper;
import com.api.kdevfolio.blog.models.Category;
import com.api.kdevfolio.blog.repositories.ArticleRepository;
import com.api.kdevfolio.blog.repositories.CategoryRepository;
import com.api.kdevfolio.common.exceptions.ConflictException;
import com.api.kdevfolio.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(UUID id) {
        return CategoryMapper.toResponse(findCategory(id));
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(CategoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsBySlug(request.slug())) {
            throw duplicate("slug", request.slug());
        }
        if (categoryRepository.existsByName(request.name())) {
            throw duplicate("name", request.name());
        }

        Category category = new Category();
        CategoryMapper.apply(request, category);

        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = findCategory(id);

        if (categoryRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw duplicate("slug", request.slug());
        }
        if (categoryRepository.existsByNameAndIdNot(request.name(), id)) {
            throw duplicate("name", request.name());
        }

        CategoryMapper.apply(request, category);

        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    /**
     * Refuses to delete a category still attached to articles; detach it from
     * those articles first. (Previously this failed with a 500 FK violation.)
     */
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = findCategory(id);

        if (articleRepository.existsByCategories_Id(id)) {
            throw new ConflictException(
                    "Category '%s' is still used by one or more articles.".formatted(category.getSlug())
            );
        }

        categoryRepository.delete(category);
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private static ConflictException duplicate(String field, String value) {
        return new ConflictException("A category with %s '%s' already exists.".formatted(field, value));
    }
}
