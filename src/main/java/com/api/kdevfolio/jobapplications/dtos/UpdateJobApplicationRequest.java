package com.api.kdevfolio.jobapplications.dtos;

import com.api.kdevfolio.jobapplications.enums.CvProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateJobApplicationRequest(

        @NotBlank
        String companyName,

        String offerUrl,

        @NotBlank
        String roleName,

        @NotNull
        CvProfile profileType,

        @NotBlank
        String jobDescription

) {}