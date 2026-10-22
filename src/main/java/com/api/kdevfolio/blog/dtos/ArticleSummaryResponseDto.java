
package com.api.kdevfolio.blog.dtos;

import com.api.kdevfolio.blog.models.ArticleLanguage;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record ArticleSummaryResponseDto(
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
