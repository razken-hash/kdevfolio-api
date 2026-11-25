package com.api.kdevfolio.analytics.mappers;

import com.api.kdevfolio.analytics.dtos.RecordVisitRequest;
import com.api.kdevfolio.analytics.dtos.VisitResponse;
import com.api.kdevfolio.analytics.models.Visit;

/**
 * Maps by field name (builder / setters) — never positionally — so that
 * reordering fields cannot silently shuffle values between properties.
 */
public final class VisitMapper {

    private VisitMapper() {}

    public static Visit toEntity(RecordVisitRequest request, String ipAddress) {
        Visit visit = new Visit();
        visit.setVisitorId(request.visitorId());
        visit.setIpAddress(ipAddress);
        visit.setOrigin(request.origin());
        visit.setPath(request.path());
        visit.setPageTitle(request.pageTitle());
        visit.setDeviceType(request.deviceType());
        visit.setOperatingSystem(request.operatingSystem());
        visit.setPlatform(request.platform());
        visit.setBrowser(request.browser());
        visit.setBrowserLanguage(request.browserLanguage());
        visit.setTimeZone(request.timeZone());
        return visit;
    }

    public static VisitResponse toResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .visitedAt(visit.getVisitedAt())
                .visitorId(visit.getVisitorId())
                .origin(visit.getOrigin())
                .path(visit.getPath())
                .pageTitle(visit.getPageTitle())
                .deviceType(visit.getDeviceType())
                .operatingSystem(visit.getOperatingSystem())
                .platform(visit.getPlatform())
                .browser(visit.getBrowser())
                .browserLanguage(visit.getBrowserLanguage())
                .timeZone(visit.getTimeZone())
                .build();
    }
}
