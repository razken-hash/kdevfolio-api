package com.api.kdevfolio.jobapplications.dtos;

import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(

        @NotNull
        ApplicationStatus status

) {}