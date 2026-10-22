
package com.api.kdevfolio.analytics.repositories;

import com.api.kdevfolio.analytics.models.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VisitsRepository extends JpaRepository<Visit, UUID> {

    List<Visit> findTop100ByOrderByVisitedAtDesc();

    List<Visit> findByVisitorIdOrderByVisitedAtDesc(UUID visitorId);

    long countByVisitorId(UUID visitorId);
}
