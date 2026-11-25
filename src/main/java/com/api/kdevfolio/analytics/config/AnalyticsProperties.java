package com.api.kdevfolio.analytics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;
import java.util.UUID;

/**
 * Bound once at startup from {@code analytics.*}. A malformed UUID in
 * EXCLUDED_VISITORS now fails fast at boot instead of on every request.
 *
 * @param excludedVisitors visitor IDs (e.g. your own browsers) whose visits are not recorded
 */
@ConfigurationProperties(prefix = "analytics")
public record AnalyticsProperties(Set<UUID> excludedVisitors) {

    public AnalyticsProperties {
        excludedVisitors = excludedVisitors == null ? Set.of() : Set.copyOf(excludedVisitors);
    }

    public boolean isExcluded(UUID visitorId) {
        return visitorId != null && excludedVisitors.contains(visitorId);
    }
}
