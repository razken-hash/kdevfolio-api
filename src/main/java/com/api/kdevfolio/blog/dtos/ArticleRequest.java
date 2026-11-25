package com.api.kdevfolio.blog.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

/** Used for both create (POST) and full update (PUT). */
public record ArticleRequest(
        @NotBlank @Size(max = 180) String slug,
        @NotNull @Positive Integer readingTimeMinutes,
        @NotBlank @Size(max = 150) String authorName,
        Set<UUID> categoryIds,
        @NotEmpty Set<@Valid @NotNull ArticleContentRequest> translations
) {}
