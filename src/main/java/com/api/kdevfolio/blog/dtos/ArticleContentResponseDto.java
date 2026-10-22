package com.api.kdevfolio.blog.dtos;

import com.api.kdevfolio.blog.models.ArticleLanguage;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ArticleContentResponseDto(
        UUID id,
        ArticleLanguage language,
        String title,
        String description,
        String content,
        String imageUrl
) {}