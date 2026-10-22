package com.api.kdevfolio.blog.services;

import com.api.kdevfolio.blog.dtos.ArticleContentResponseDto;
import com.api.kdevfolio.blog.dtos.ArticleResponseDto;
import com.api.kdevfolio.blog.dtos.ArticleSummaryResponseDto;
import com.api.kdevfolio.blog.dtos.CategoryResponseDto;
import com.api.kdevfolio.blog.dtos.CreateArticleContentRequestDto;
import com.api.kdevfolio.blog.dtos.CreateArticleRequestDto;
import com.api.kdevfolio.blog.dtos.FullArticleResponseDto;
import com.api.kdevfolio.blog.exceptions.ArticleNotFoundException;
import com.api.kdevfolio.blog.models.Article;
import com.api.kdevfolio.blog.models.ArticleContent;
import com.api.kdevfolio.blog.models.ArticleLanguage;
import com.api.kdevfolio.blog.models.Category;
import com.api.kdevfolio.blog.repositories.ArticlesRepository;
import com.api.kdevfolio.blog.repositories.CategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArticlesService {

    private final ArticlesRepository articlesRepository;
    private final CategoriesRepository categoryRepository;

    public ArticlesService(
            ArticlesRepository articlesRepository,
            CategoriesRepository categoriesRepository
    ) {
        this.articlesRepository = articlesRepository;
        this.categoryRepository = categoriesRepository;
    }

    public ArticleResponseDto getArticleBySlug(
            String slug,
            ArticleLanguage language
    ) {
        Article article = articlesRepository.findBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Article not found with slug: " + slug
                ));

        return toArticleResponseDto(article, language);
    }

    public ArticleResponseDto getArticleById(
            UUID id,
            ArticleLanguage language
    ) {
        return toArticleResponseDto(getArticleEntityById(id), language);
    }

    public List<ArticleSummaryResponseDto> getAllArticles(
            ArticleLanguage language
    ) {
        return articlesRepository.findAll()
                .stream()
                .filter(article -> hasTranslation(article, language))
                .map(article -> toArticleSummaryResponseDto(article, language))
                .toList();
    }

    public List<ArticleSummaryResponseDto> getArticlesByCategorySlug(
            String categorySlug,
            ArticleLanguage language
    ) {
        return articlesRepository.findAllByCategories_Slug(categorySlug)
                .stream()
                .filter(article -> hasTranslation(article, language))
                .map(article -> toArticleSummaryResponseDto(article, language))
                .toList();
    }

    @Transactional
    public FullArticleResponseDto addArticle(
            CreateArticleRequestDto request
    ) {
        Article article = new Article();

        article.setSlug(request.slug());
        article.setReadingTimeMinutes(request.readingTimeMinutes());
        article.setAuthorName(request.authorName());
        article.setCategories(resolveCategories(request.categoryIds()));

        for (CreateArticleContentRequestDto translationRequest
                : request.translations()) {

            ArticleContent translation = new ArticleContent();

            translation.setLanguage(translationRequest.language());
            translation.setTitle(translationRequest.title());
            translation.setDescription(translationRequest.description());
            translation.setContent(translationRequest.content());
            translation.setImageUrl(translationRequest.imageUrl());

            article.addTranslation(translation);
        }

        Article savedArticle = articlesRepository.save(article);

        return toFullArticleResponseDto(savedArticle);
    }

    @Transactional
    public FullArticleResponseDto updateArticle(
            UUID id,
            CreateArticleRequestDto request
    ) {
        Article article = getArticleEntityById(id);

        article.setSlug(request.slug());
        article.setReadingTimeMinutes(request.readingTimeMinutes());
        article.setAuthorName(request.authorName());
        article.setCategories(resolveCategories(request.categoryIds()));

        Map<ArticleLanguage, ArticleContent> existingByLanguage =
                article.getTranslations()
                        .stream()
                        .collect(Collectors.toMap(
                                ArticleContent::getLanguage,
                                Function.identity()
                        ));

        Set<ArticleLanguage> updatedLanguages = new HashSet<>();

        for (CreateArticleContentRequestDto translationRequest
                : request.translations()) {

            ArticleLanguage language = translationRequest.language();
            updatedLanguages.add(language);

            ArticleContent translation = existingByLanguage.get(language);

            if (translation == null) {
                translation = new ArticleContent();
                translation.setLanguage(language);
                article.addTranslation(translation);
            }

            translation.setTitle(translationRequest.title());
            translation.setDescription(translationRequest.description());
            translation.setContent(translationRequest.content());
            translation.setImageUrl(translationRequest.imageUrl());
        }

        // Remove translations omitted from the update request.
        for (ArticleContent translation :
                new HashSet<>(article.getTranslations())) {

            if (!updatedLanguages.contains(translation.getLanguage())) {
                article.removeTranslation(translation);
            }
        }

        Article savedArticle = articlesRepository.save(article);

        return toFullArticleResponseDto(savedArticle);
    }

    @Transactional
    public void deleteArticle(UUID id) {
        Article article = getArticleEntityById(id);
        articlesRepository.delete(article);
    }

    private Article getArticleEntityById(UUID id) {
        return articlesRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Article not found with id: " + id
                ));
    }

    private Set<Category> resolveCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Category> categories =
                categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new IllegalArgumentException(
                    "One or more category IDs do not exist."
            );
        }

        return new HashSet<>(categories);
    }

    private boolean hasTranslation(
            Article article,
            ArticleLanguage language
    ) {
        return article.getTranslations()
                .stream()
                .anyMatch(t -> t.getLanguage() == language);
    }

    private ArticleContent getTranslation(
            Article article,
            ArticleLanguage language
    ) {
        return article.getTranslations()
                .stream()
                .filter(t -> t.getLanguage() == language)
                .findFirst()
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Translation not found for article "
                                + article.getId()
                                + " in language: " + language
                ));
    }

    private ArticleResponseDto toArticleResponseDto(
            Article article,
            ArticleLanguage language
    ) {
        ArticleContent translation = getTranslation(article, language);

        return ArticleResponseDto.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
                .categories(article.getCategories()
                        .stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()))
                .language(translation.getLanguage())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .content(translation.getContent())
                .imageUrl(translation.getImageUrl())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private ArticleSummaryResponseDto toArticleSummaryResponseDto(
            Article article,
            ArticleLanguage language
    ) {
        ArticleContent translation = getTranslation(article, language);

        return ArticleSummaryResponseDto.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
                .categories(article.getCategories()
                        .stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()))
                .language(translation.getLanguage())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .imageUrl(translation.getImageUrl())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private FullArticleResponseDto toFullArticleResponseDto(
            Article article
    ) {
        return FullArticleResponseDto.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .readingTimeMinutes(article.getReadingTimeMinutes())
                .authorName(article.getAuthorName())
//                .categories(article.getCategories()
//                        .stream()
//                        .map(this::toCategoryResponseDto)
//                        .toList())
                .translations(article.getTranslations()
                        .stream()
                        .map(this::toArticleContentResponseDto)
                        .toList())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private CategoryResponseDto toCategoryResponseDto(
            Category category
    ) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .slug(category.getSlug())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    private ArticleContentResponseDto toArticleContentResponseDto(
            ArticleContent translation
    ) {
        return ArticleContentResponseDto.builder()
                .id(translation.getId())
                .language(translation.getLanguage())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .content(translation.getContent())
                .imageUrl(translation.getImageUrl())
                .build();
    }
}
