package com.api.kdevfolio.blog.controllers;

import com.api.kdevfolio.blog.dtos.ArticleRequest;
import com.api.kdevfolio.blog.dtos.ArticleResponse;
import com.api.kdevfolio.blog.dtos.ArticleSummaryResponse;
import com.api.kdevfolio.blog.dtos.FullArticleResponse;
import com.api.kdevfolio.blog.models.ArticleLanguage;
import com.api.kdevfolio.blog.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blog/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleSummaryResponse> getAllArticles(
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articleService.getAllArticles(language);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticleById(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articleService.getArticleById(id, language);
    }

    @GetMapping("/slug/{slug}")
    public ArticleResponse getArticleBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articleService.getArticleBySlug(slug, language);
    }

    @GetMapping("/category/{categorySlug}")
    public List<ArticleSummaryResponse> getArticlesByCategory(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articleService.getArticlesByCategorySlug(categorySlug, language);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullArticleResponse createArticle(@Valid @RequestBody ArticleRequest request) {
        return articleService.createArticle(request);
    }

    @PutMapping("/{id}")
    public FullArticleResponse updateArticle(
            @PathVariable UUID id,
            @Valid @RequestBody ArticleRequest request
    ) {
        return articleService.updateArticle(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
    }
}
