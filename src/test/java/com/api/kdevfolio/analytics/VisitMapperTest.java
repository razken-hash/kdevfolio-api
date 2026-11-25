package com.api.kdevfolio.analytics;

import com.api.kdevfolio.analytics.dtos.RecordVisitRequest;
import com.api.kdevfolio.analytics.dtos.VisitResponse;
import com.api.kdevfolio.analytics.mappers.VisitMapper;
import com.api.kdevfolio.analytics.models.Visit;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression test: every field must land in the property of the same name
 * (previously deviceType held the language, browser held the OS, etc.).
 */
class VisitMapperTest {

    private final UUID visitorId = UUID.randomUUID();

    private final RecordVisitRequest request = new RecordVisitRequest(
            visitorId,
            "/admin/analytics",
            "direct",
            "KDevfolio",
            "desktop",
            "macOS",
            "MacIntel",
            "Chrome",
            "en-FR",
            "Europe/Paris"
    );

    @Test
    void roundTripKeepsEveryFieldInItsOwnProperty() {
        Visit visit = VisitMapper.toEntity(request, "203.0.113.7");
        visit.setId(UUID.randomUUID());
        visit.setVisitedAt(Instant.parse("2026-11-25T11:37:26Z"));

        VisitResponse response = VisitMapper.toResponse(visit);

        assertThat(visit.getIpAddress()).isEqualTo("203.0.113.7");
        assertThat(response.id()).isEqualTo(visit.getId());
        assertThat(response.visitedAt()).isEqualTo(visit.getVisitedAt());
        assertThat(response.visitorId()).isEqualTo(visitorId);
        assertThat(response.origin()).isEqualTo("direct");
        assertThat(response.path()).isEqualTo("/admin/analytics");
        assertThat(response.pageTitle()).isEqualTo("KDevfolio");
        assertThat(response.deviceType()).isEqualTo("desktop");
        assertThat(response.operatingSystem()).isEqualTo("macOS");
        assertThat(response.platform()).isEqualTo("MacIntel");
        assertThat(response.browser()).isEqualTo("Chrome");
        assertThat(response.browserLanguage()).isEqualTo("en-FR");
        assertThat(response.timeZone()).isEqualTo("Europe/Paris");
    }
}
