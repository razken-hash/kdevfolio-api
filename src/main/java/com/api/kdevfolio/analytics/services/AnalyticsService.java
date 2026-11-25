package com.api.kdevfolio.analytics.services;

import com.api.kdevfolio.analytics.config.AnalyticsProperties;
import com.api.kdevfolio.analytics.dtos.RecordVisitRequest;
import com.api.kdevfolio.analytics.dtos.VisitResponse;
import com.api.kdevfolio.analytics.mappers.VisitMapper;
import com.api.kdevfolio.analytics.repositories.VisitRepository;
import com.api.kdevfolio.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final VisitRepository visitRepository;
    private final AnalyticsProperties properties;

    /**
     * @return the recorded visit, or empty if the visitor is excluded
     */
    @Transactional
    public Optional<VisitResponse> recordVisit(RecordVisitRequest request, String ipAddress) {
        if (properties.isExcluded(request.visitorId())) {
            return Optional.empty();
        }

        var saved = visitRepository.save(VisitMapper.toEntity(request, ipAddress));
        return Optional.of(VisitMapper.toResponse(saved));
    }

    public List<VisitResponse> getRecentVisits() {
        return visitRepository.findTop100ByOrderByVisitedAtDesc()
                .stream()
                .map(VisitMapper::toResponse)
                .toList();
    }

    public VisitResponse getVisitById(UUID id) {
        return visitRepository.findById(id)
                .map(VisitMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Visit", "id", id));
    }
}
