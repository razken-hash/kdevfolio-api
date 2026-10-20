
package com.api.kdevfolio.blog.controllers;

import com.api.kdevfolio.blog.models.Article;
import com.api.kdevfolio.blog.services.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return blogService.getAllArticle();
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable UUID id) {
        return blogService.getArticleById(id);
    }

    @GetMapping("/slug/{slug}")
    public Article getArticleBySlug(@PathVariable String slug) {
        return blogService.getArticleBySlug(slug);
    }

    @GetMapping("/category/{categorySlug}")
    public List<Article> getArticlesByCategory(
            @PathVariable String categorySlug
    ) {
        return blogService.getArticlesByCategorySlug(categorySlug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article addArticle(@RequestBody Article article) {
        return blogService.addArticle(article);
    }

    @PutMapping("/{id}")
    public Article updateArticle(
            @PathVariable UUID id,
            @RequestBody Article article
    ) {
        return blogService.updateArticle(id, article);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable UUID id) {
        blogService.deleteArticle(id);
    }
}
