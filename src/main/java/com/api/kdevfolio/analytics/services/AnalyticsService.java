
package com.api.kdevfolio.analytics.services;

import com.api.kdevfolio.analytics.dtos.RecordVisitDto;
import com.api.kdevfolio.analytics.dtos.VisitResponseDto;
import com.api.kdevfolio.analytics.models.Visit;
import com.api.kdevfolio.analytics.repositories.VisitsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VisitsRepository visitsRepository;

    @Transactional
    public VisitResponseDto recordVisit(
            RecordVisitDto dto,
            HttpServletRequest request
    ) {
        Visit visit = new Visit();

        visit.setVisitorId(dto.visitorId());
        visit.setPath(dto.path());
        visit.setOrigin(dto.origin());
        visit.setPageTitle(dto.pageTitle());
        visit.setBrowserLanguage(dto.browserLanguage());
        visit.setDeviceType(dto.deviceType());
        visit.setBrowser(dto.browser());
        visit.setOperatingSystem(dto.operatingSystem());
        visit.setViewportWidth(dto.viewportWidth());
        visit.setViewportHeight(dto.viewportHeight());
        visit.setScreenWidth(dto.screenWidth());
        visit.setScreenHeight(dto.screenHeight());
        visit.setTimeZone(dto.timeZone());
        visit.setPlatform(dto.platform());

        // Obtain the address from the server-side request.
        // Configure trusted proxy handling before relying on this
        // value in a reverse-proxy deployment.
        visit.setIpAddress(request.getRemoteAddr());

        // Country can be populated here using an IP geolocation
        // provider if required.

        Visit savedVisit = visitsRepository.save(visit);

        return toResponseDto(savedVisit);
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDto> getRecentVisits() {
        return visitsRepository.findTop100ByOrderByVisitedAtDesc()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VisitResponseDto getVisitById(UUID id) {
        Visit visit = visitsRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Visit not found: " + id
                        )
                );

        return toResponseDto(visit);
    }

    private VisitResponseDto toResponseDto(Visit visit) {
        return new VisitResponseDto(
                visit.getId(),
                visit.getVisitedAt(),
                visit.getVisitorId(),
                visit.getCountry(),
                visit.getOrigin(),
                visit.getPath(),
                visit.getPageTitle(),
                visit.getBrowserLanguage(),
                visit.getDeviceType(),
                visit.getBrowser(),
                visit.getOperatingSystem(),
                visit.getViewportWidth(),
                visit.getViewportHeight(),
                visit.getScreenWidth(),
                visit.getScreenHeight(),
                visit.getTimeZone(),
                visit.getPlatform()
        );
    }
}
