
package com.api.kdevfolio.blog.repositories;

import com.api.kdevfolio.blog.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriesRepository
        extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
