
package com.api.kdevfolio.blog.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record CreateArticleRequestDto(
        @NotBlank
        @Size(max = 180)
        String slug,

        @NotNull
        @Positive
        Integer readingTimeMinutes,

        @NotBlank
        @Size(max = 150)
        String authorName,

        Set<UUID> categoryIds,

        @NotNull
        @Size(min = 1)
        Set<@Valid CreateArticleContentRequestDto> translations
) {}


