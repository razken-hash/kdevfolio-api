package com.api.kdevfolio.analytics;

import com.api.kdevfolio.analytics.config.AnalyticsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsPropertiesTest {

    private static AnalyticsProperties bind(String excludedVisitors) {
        var source = new MapConfigurationPropertySource(
                Map.of("analytics.excluded-visitors", excludedVisitors)
        );
        return new Binder(source).bindOrCreate("analytics", AnalyticsProperties.class);
    }

    @Test
    void emptyValueMeansNoExclusions() {
        AnalyticsProperties properties = bind("");

        assertThat(properties.excludedVisitors()).isEmpty();
        assertThat(properties.isExcluded(UUID.randomUUID())).isFalse();
        assertThat(properties.isExcluded(null)).isFalse();
    }

    @Test
    void commaSeparatedUuidsAreExcluded() {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        AnalyticsProperties properties = bind(first + ", " + second);

        assertThat(properties.isExcluded(first)).isTrue();
        assertThat(properties.isExcluded(second)).isTrue();
        assertThat(properties.isExcluded(UUID.randomUUID())).isFalse();
    }
}
