package com.api.kdevfolio.blog.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** An article with all of its translations (returned by create/update). */
@Builder
public record FullArticleResponse(
        UUID id,
        String slug,
        Integer readingTimeMinutes,
        String authorName,
        List<CategoryResponse> categories,
        List<ArticleContentResponse> translations,
        Instant createdAt,
        Instant updatedAt
) {}
