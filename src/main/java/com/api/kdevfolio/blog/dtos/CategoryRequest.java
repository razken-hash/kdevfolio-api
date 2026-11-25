package com.api.kdevfolio.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Used for both create (POST) and full update (PUT). */
public record CategoryRequest(
        @NotBlank @Size(max = 100) String slug,
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 500) String description
) {}
