package com.api.kdevfolio.blog.repositories;

import com.api.kdevfolio.blog.models.Article;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlogRepository extends Repository<Article, UUID> {

    Optional<Article> findById(UUID id);

    Optional<Article> findBySlug(String slug);

    List<Article> findAll();

    List<Article> findAllByCategories_Slug(String categorySlug);

    Article save(Article article);

    void delete(Article article);
}