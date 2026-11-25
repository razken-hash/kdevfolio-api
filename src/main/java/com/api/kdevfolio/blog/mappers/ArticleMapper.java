package com.api.kdevfolio.blog.mappers;

import com.api.kdevfolio.blog.dtos.ArticleContentRequest;
import com.api.kdevfolio.blog.dtos.ArticleContentResponse;
import com.api.kdevfolio.blog.dtos.ArticleResponse;
import com.api.kdevfolio.blog.dtos.ArticleSummaryResponse;
import com.api.kdevfolio.blog.dtos.FullArticleResponse;
import com.api.kdevfolio.blog.models.Article;
import com.api.kdevfolio.blog.models.ArticleContent;
import com.api.kdevfolio.blog.models.Category;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public final class ArticleMapper {

    private ArticleMapper() {}

    public static void apply(ArticleContentRequest request, ArticleContent content) {
        content.setLanguage(request.language());
        content.setTitle(request.title());
        content.setDescription(request.description());
        content.setContent(request.content());
        content.setImageUrl(request.imageUrl());
    }

    public static ArticleResponse toResponse(Article article, ArticleContent translation) {
        return ArticleResponse.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .imageUrl(translation.getImageUrl())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
                .categories(categoryNames(article))
                .language(translation.getLanguage())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .content(translation.getContent())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public static ArticleSummaryResponse toSummary(Article article, ArticleContent translation) {
        return ArticleSummaryResponse.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .imageUrl(translation.getImageUrl())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
                .categories(categoryNames(article))
                .language(translation.getLanguage())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public static FullArticleResponse toFullResponse(Article article) {
        return FullArticleResponse.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
                .categories(article.getCategories().stream()
                        .sorted(Comparator.comparing(Category::getName))
                        .map(CategoryMapper::toResponse)
                        .toList())
                .translations(article.getTranslations().stream()
                        .sorted(Comparator.comparing(ArticleContent::getLanguage))
                        .map(ArticleMapper::toContentResponse)
                        .toList())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public static ArticleContentResponse toContentResponse(ArticleContent content) {
        return ArticleContentResponse.builder()
                .id(content.getId())
                .language(content.getLanguage())
                .title(content.getTitle())
                .description(content.getDescription())
                .content(content.getContent())
                .imageUrl(content.getImageUrl())
                .build();
    }

    private static Set<String> categoryNames(Article article) {
        return article.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }
}
