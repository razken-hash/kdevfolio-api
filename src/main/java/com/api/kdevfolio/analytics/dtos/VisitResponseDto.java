
package com.api.kdevfolio.analytics.dtos;

import java.time.Instant;
import java.util.UUID;

public record VisitResponseDto(

        UUID id,

        Instant visitedAt,

        UUID visitorId,

        String country,

        String origin,

        String path,

        String pageTitle,

        String browserLanguage,

        String deviceType,

        String browser,

        String operatingSystem,

        Integer viewportWidth,

        Integer viewportHeight,

        Integer screenWidth,

        Integer screenHeight,

        String timeZone,

        String platform

) {}
