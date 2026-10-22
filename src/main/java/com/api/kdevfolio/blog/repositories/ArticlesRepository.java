
package com.api.kdevfolio.blog.repositories;

import com.api.kdevfolio.blog.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticlesRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Article> findAllByCategories_Slug(String categorySlug);
}
