
package com.api.kdevfolio.analytics.controllers;

import com.api.kdevfolio.analytics.dtos.RecordVisitDto;
import com.api.kdevfolio.analytics.dtos.VisitResponseDto;
import com.api.kdevfolio.analytics.services.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/visits")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitResponseDto recordVisit(
            @Valid @RequestBody RecordVisitDto dto,
            HttpServletRequest request
    ) {
        return analyticsService.recordVisit(dto, request);
    }

    @GetMapping
    public List<VisitResponseDto> getRecentVisits() {
        return analyticsService.getRecentVisits();
    }

    @GetMapping("/{id}")
    public VisitResponseDto getVisitById(
            @PathVariable UUID id
    ) {
        return analyticsService.getVisitById(id);
    }
}
