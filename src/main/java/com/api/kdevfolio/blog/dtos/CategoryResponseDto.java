package com.api.kdevfolio.blog.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CategoryResponseDto(

        UUID id,

        String slug,

        String name,

        String description,

        Instant createdAt,

        Instant updatedAt

) {}