package com.api.kdevfolio.blog.repositories;

import com.api.kdevfolio.blog.models.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Read methods fetch translations and categories eagerly via an entity graph,
 * avoiding one extra query per article (N+1) when mapping to DTOs.
 */
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Override
    @EntityGraph(attributePaths = {"translations", "categories"})
    Optional<Article> findById(UUID id);

    @EntityGraph(attributePaths = {"translations", "categories"})
    Optional<Article> findBySlug(String slug);

    @EntityGraph(attributePaths = {"translations", "categories"})
    List<Article> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"translations", "categories"})
    List<Article> findAllByCategories_SlugOrderByCreatedAtDesc(String categorySlug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, UUID id);

    boolean existsByCategories_Id(UUID categoryId);
}
