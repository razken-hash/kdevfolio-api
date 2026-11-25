package com.api.kdevfolio.blog.services;

import com.api.kdevfolio.blog.dtos.ArticleContentRequest;
import com.api.kdevfolio.blog.dtos.ArticleRequest;
import com.api.kdevfolio.blog.dtos.ArticleResponse;
import com.api.kdevfolio.blog.dtos.ArticleSummaryResponse;
import com.api.kdevfolio.blog.dtos.FullArticleResponse;
import com.api.kdevfolio.blog.mappers.ArticleMapper;
import com.api.kdevfolio.blog.models.Article;
import com.api.kdevfolio.blog.models.ArticleContent;
import com.api.kdevfolio.blog.models.ArticleLanguage;
import com.api.kdevfolio.blog.models.Category;
import com.api.kdevfolio.blog.repositories.ArticleRepository;
import com.api.kdevfolio.blog.repositories.CategoryRepository;
import com.api.kdevfolio.common.exceptions.BadRequestException;
import com.api.kdevfolio.common.exceptions.ConflictException;
import com.api.kdevfolio.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    // ---- Queries -----------------------------------------------------------

    public List<ArticleSummaryResponse> getAllArticles(ArticleLanguage language) {
        return toSummaries(articleRepository.findAllByOrderByCreatedAtDesc(), language);
    }

    public List<ArticleSummaryResponse> getArticlesByCategorySlug(
            String categorySlug,
            ArticleLanguage language
    ) {
        return toSummaries(
                articleRepository.findAllByCategories_SlugOrderByCreatedAtDesc(categorySlug),
                language
        );
    }

    public ArticleResponse getArticleById(UUID id, ArticleLanguage language) {
        Article article = findArticle(id);
        return ArticleMapper.toResponse(article, requireTranslation(article, language));
    }

    public ArticleResponse getArticleBySlug(String slug, ArticleLanguage language) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "slug", slug));
        return ArticleMapper.toResponse(article, requireTranslation(article, language));
    }

    // ---- Commands ----------------------------------------------------------

    @Transactional
    public FullArticleResponse createArticle(ArticleRequest request) {
        if (articleRepository.existsBySlug(request.slug())) {
            throw new ConflictException("An article with slug '%s' already exists.".formatted(request.slug()));
        }

        Article article = new Article();
        applyRequest(request, article);

        return ArticleMapper.toFullResponse(articleRepository.save(article));
    }

    @Transactional
    public FullArticleResponse updateArticle(UUID id, ArticleRequest request) {
        if (articleRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new ConflictException("An article with slug '%s' already exists.".formatted(request.slug()));
        }

        Article article = findArticle(id);
        applyRequest(request, article);

        return ArticleMapper.toFullResponse(articleRepository.save(article));
    }

    @Transactional
    public void deleteArticle(UUID id) {
        articleRepository.delete(findArticle(id));
    }

    // ---- Helpers -----------------------------------------------------------

    private Article findArticle(UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
    }

    /**
     * Applies a full (PUT-style) request: translations missing from the
     * request are removed, existing languages are updated in place, and
     * new languages are added.
     */
    private void applyRequest(ArticleRequest request, Article article) {
        ensureUniqueLanguages(request.translations());

        article.setSlug(request.slug());
        article.setReadingTimeMinutes(request.readingTimeMinutes());
        article.setAuthorName(request.authorName());
        article.setCategories(resolveCategories(request.categoryIds()));

        Map<ArticleLanguage, ArticleContent> existing = article.getTranslations().stream()
                .collect(Collectors.toMap(ArticleContent::getLanguage, Function.identity()));

        Set<ArticleLanguage> requested = EnumSet.noneOf(ArticleLanguage.class);

        for (ArticleContentRequest translationRequest : request.translations()) {
            requested.add(translationRequest.language());

            ArticleContent translation = existing.get(translationRequest.language());
            if (translation == null) {
                translation = new ArticleContent();
                article.addTranslation(translation);
            }
            ArticleMapper.apply(translationRequest, translation);
        }

        existing.values().stream()
                .filter(translation -> !requested.contains(translation.getLanguage()))
                .forEach(article::removeTranslation);
    }

    private void ensureUniqueLanguages(Set<ArticleContentRequest> translations) {
        Set<ArticleLanguage> seen = EnumSet.noneOf(ArticleLanguage.class);
        for (ArticleContentRequest translation : translations) {
            if (!seen.add(translation.language())) {
                throw new BadRequestException(
                        "Duplicate translation for language: " + translation.language()
                );
            }
        }
    }

    private Set<Category> resolveCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            Set<UUID> found = categories.stream().map(Category::getId).collect(Collectors.toSet());
            Set<UUID> missing = new HashSet<>(categoryIds);
            missing.removeAll(found);
            throw new BadRequestException("Unknown category IDs: " + missing);
        }

        return new HashSet<>(categories);
    }

    private List<ArticleSummaryResponse> toSummaries(List<Article> articles, ArticleLanguage language) {
        return articles.stream()
                .flatMap(article -> findTranslation(article, language)
                        .map(translation -> ArticleMapper.toSummary(article, translation))
                        .stream())
                .toList();
    }

    private Optional<ArticleContent> findTranslation(Article article, ArticleLanguage language) {
        return article.getTranslations().stream()
                .filter(translation -> translation.getLanguage() == language)
                .findFirst();
    }

    private ArticleContent requireTranslation(Article article, ArticleLanguage language) {
        return findTranslation(article, language)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Translation not found for article %s in language: %s"
                                .formatted(article.getId(), language)
                ));
    }
}
