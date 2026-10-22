
package com.api.kdevfolio.analytics.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RecordVisitDto(

        UUID visitorId,

        @NotBlank
        @Size(max = 2048)
        String path,

        @Size(max = 50)
        String origin,

        @Size(max = 300)
        String pageTitle,

        @Size(max = 20)
        String deviceType,

        @Size(max = 50)
        String operatingSystem,

        @Size(max = 100)
        String platform,

        @Size(max = 50)
        String browser,

        @Size(max = 10)
        String browserLanguage,

        @Size(max = 100)
        String timeZone

) {}
