package com.api.kdevfolio.health.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Health {
    private HealthStatus status;
}