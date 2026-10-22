
package com.api.kdevfolio.analytics.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "portfolio_visits",
        indexes = {
                @Index(name = "idx_visit_time", columnList = "visited_at"),
                @Index(name = "idx_visit_path", columnList = "path"),
                @Index(name = "idx_visit_country", columnList = "country"),
                @Index(name = "idx_visit_visitor", columnList = "visitor_id"),
                @Index(name = "idx_visit_origin", columnList = "origin"),
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Visit {

    // Unique identifier for each page-view event.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Server-generated UTC timestamp.
    @Column(name = "visited_at", nullable = false, updatable = false)
    private Instant visitedAt;

    // Pseudonymous browser identifier, when permitted.
    @Column(name = "visitor_id")
    private UUID visitorId;

    // IP address: personal data; apply appropriate retention controls.
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // Approximate country inferred from IP geolocation.
    @Column(length = 50)
    private String country;

    // Normalized acquisition source, e.g. linkedin, github, direct.
    @Column(length = 50)
    private String origin;

    // Angular route or page path, preferably without sensitive query data.
    @Column(nullable = false, length = 2048)
    private String path;

    // Page title at the time of the visit.
    @Column(name = "page_title", length = 300)
    private String pageTitle;

    // Browser or interface language, e.g. fr, en, ar.
    @Column(name = "browser_language", length = 10)
    private String browserLanguage;

    // Broad category: desktop, mobile, tablet, or unknown.
    @Column(name = "device_type", length = 20)
    private String deviceType;

    // Parsed browser family, e.g. Chrome, Firefox, Safari.
    @Column(length = 50)
    private String browser;

    // Parsed operating system, e.g. macOS, Windows, Linux, Android.
    @Column(name = "operating_system", length = 50)
    private String operatingSystem;

    // Browser viewport dimensions in CSS pixels.
    @Column(name = "viewport_width")
    private Integer viewportWidth;

    @Column(name = "viewport_height")
    private Integer viewportHeight;

    // Screen dimensions reported by the browser.
    @Column(name = "screen_width")
    private Integer screenWidth;

    @Column(name = "screen_height")
    private Integer screenHeight;

    // Browser-reported timezone; not proof of geographic location.
    @Column(name = "time_zone", length = 100)
    private String timeZone;

    // Browser-reported platform; optional and approximate.
    @Column(length = 100)
    private String platform;

    @PrePersist
    protected void onCreate() {
        if (visitedAt == null) {
            visitedAt = Instant.now();
        }
    }
}
