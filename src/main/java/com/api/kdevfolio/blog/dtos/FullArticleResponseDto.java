package com.api.kdevfolio.blog.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record FullArticleResponseDto(
        UUID id,
        String slug,
        Integer readingTimeMinutes,
        String authorName,
        List<CategoryResponseDto> categories,
        List<ArticleContentResponseDto> translations,
        Instant createdAt,
        Instant updatedAt
) {}