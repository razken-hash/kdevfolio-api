
package com.api.kdevfolio.blog.services;

import com.api.kdevfolio.blog.exceptions.ArticleNotFoundException;
import com.api.kdevfolio.blog.models.Article;
import com.api.kdevfolio.blog.models.ArticleContent;
import com.api.kdevfolio.blog.repositories.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Article getArticleBySlug(String slug) {
        return blogRepository.findBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Article not found with slug: " + slug
                ));
    }

    public Article getArticleById(UUID id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Article not found with id: " + id
                ));
    }

    public List<Article> getAllArticles() {
        return blogRepository.findAll();
    }

    public List<Article> getArticlesByCategorySlug(String categorySlug) {
        return blogRepository.findAllByCategories_Slug(categorySlug);
    }

    @Transactional
    public Article addArticle(Article article) {
        return blogRepository.save(article);
    }

    @Transactional
    public void deleteArticle(UUID id) {
        Article article = getArticleById(id);
        blogRepository.delete(article);
    }

    @Transactional
    public Article updateArticle(UUID id, Article updatedArticle) {
        Article existingArticle = getArticleById(id);

        // Update shared article fields
        existingArticle.setSlug(updatedArticle.getSlug());
        existingArticle.setImageUrl(updatedArticle.getImageUrl());
        existingArticle.setReadingTimeMinutes(
                updatedArticle.getReadingTimeMinutes()
        );
        existingArticle.setAuthorName(updatedArticle.getAuthorName());
        existingArticle.setCategories(updatedArticle.getCategories());

        // Replace translations
        List<ArticleContent> newTranslations =
                new ArrayList<>(updatedArticle.getTranslations());

        for (ArticleContent translation :
                new ArrayList<>(existingArticle.getTranslations())) {
            existingArticle.removeTranslation(translation);
        }

        for (ArticleContent translation : newTranslations) {
            existingArticle.addTranslation(translation);
        }

        return blogRepository.save(existingArticle);
    }
}
