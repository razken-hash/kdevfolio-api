package com.api.kdevfolio.blog.dtos;

import com.api.kdevfolio.blog.models.ArticleLanguage;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record ArticleResponseDto(
        UUID id,
        String slug,
        String imageUrl,
        Integer readingTimeMinutes,
        String authorName,
        Set<String> categories,
        ArticleLanguage language,
        String title,
        String description,
        String content,
        Instant createdAt,
        Instant updatedAt
) {}