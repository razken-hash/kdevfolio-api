package com.api.kdevfolio.blog.dtos;

import com.api.kdevfolio.blog.models.ArticleLanguage;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/** An article rendered in a single language, without its content (for listings). */
@Builder
public record ArticleSummaryResponse(
        UUID id,
        String slug,
        String imageUrl,
        Integer readingTimeMinutes,
        String authorName,
        Set<String> categories,
        ArticleLanguage language,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
