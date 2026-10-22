
package com.api.kdevfolio.blog.dtos;

import com.api.kdevfolio.blog.models.ArticleLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateArticleContentRequestDto(
        @NotNull
        ArticleLanguage language,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        @Size(max = 500)
        String description,

        @NotBlank
        String content,

        @Size(max = 2048)
        String imageUrl
) {}
