package com.api.kdevfolio.health.controllers;

import com.api.kdevfolio.health.models.Health;
import com.api.kdevfolio.health.models.HealthStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Health health() {
        return new Health(HealthStatus.UP);
    }
}