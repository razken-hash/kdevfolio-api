package com.api.kdevfolio.analytics.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record VisitResponse(
        UUID id,
        Instant visitedAt,
        UUID visitorId,
        String origin,
        String path,
        String pageTitle,
        String deviceType,
        String operatingSystem,
        String platform,
        String browser,
        String browserLanguage,
        String timeZone
) {}
