package com.api.kdevfolio.analytics.controllers;

import com.api.kdevfolio.analytics.dtos.RecordVisitRequest;
import com.api.kdevfolio.analytics.dtos.VisitResponse;
import com.api.kdevfolio.analytics.services.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/visits")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * 201 with the visit when recorded, 204 when the visitor is excluded.
     * The client IP is resolved from X-Forwarded-For by the servlet container
     * (server.forward-headers-strategy=native) when behind Render's proxy.
     */
    @PostMapping
    public ResponseEntity<VisitResponse> recordVisit(
            @Valid @RequestBody RecordVisitRequest request,
            HttpServletRequest httpRequest
    ) {
        return analyticsService.recordVisit(request, httpRequest.getRemoteAddr())
                .map(visit -> ResponseEntity.status(HttpStatus.CREATED).body(visit))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public List<VisitResponse> getRecentVisits() {
        return analyticsService.getRecentVisits();
    }

    @GetMapping("/{id}")
    public VisitResponse getVisitById(@PathVariable UUID id) {
        return analyticsService.getVisitById(id);
    }
}
