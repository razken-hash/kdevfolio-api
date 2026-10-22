
package com.api.kdevfolio.blog.controllers;

import com.api.kdevfolio.blog.dtos.ArticleResponseDto;
import com.api.kdevfolio.blog.dtos.ArticleSummaryResponseDto;
import com.api.kdevfolio.blog.dtos.CreateArticleRequestDto;
import com.api.kdevfolio.blog.dtos.FullArticleResponseDto;
import com.api.kdevfolio.blog.models.ArticleLanguage;
import com.api.kdevfolio.blog.services.ArticlesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blog/articles")
public class ArticlesController {

    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping
    public List<ArticleSummaryResponseDto> getAllArticles(
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articlesService.getAllArticles(language);
    }

    @GetMapping("/{id}")
    public ArticleResponseDto getArticleById(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articlesService.getArticleById(id, language);
    }

    @GetMapping("/slug/{slug}")
    public ArticleResponseDto getArticleBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articlesService.getArticleBySlug(slug, language);
    }

    @GetMapping("/category/{categorySlug}")
    public List<ArticleSummaryResponseDto> getArticlesByCategory(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "EN") ArticleLanguage language
    ) {
        return articlesService.getArticlesByCategorySlug(
                categorySlug, language
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullArticleResponseDto createArticle(
            @Valid @RequestBody CreateArticleRequestDto request
    ) {
        return articlesService.addArticle(request);
    }

    @PutMapping("/{id}")
    public FullArticleResponseDto updateArticle(
            @PathVariable UUID id,
            @Valid @RequestBody CreateArticleRequestDto request
    ) {
        return articlesService.updateArticle(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable UUID id) {
        articlesService.deleteArticle(id);
    }
}
