
package com.api.kdevfolio.analytics.dtos;

import java.time.Instant;
import java.util.UUID;

public record VisitResponseDto(

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
