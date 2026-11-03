package com.api.kdevfolio.jobapplications.dtos;

import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.enums.CvProfile;

import java.time.Instant;

public record JobApplicationResponse(

        String id,

        String companyName,

        String offerUrl,

        String roleName,

        CvProfile profileType,

        String jobDescription,

        String latexCode,

        String pdfUrl,

        ApplicationStatus status,

        Instant createdAt,

        Instant updatedAt

) {}