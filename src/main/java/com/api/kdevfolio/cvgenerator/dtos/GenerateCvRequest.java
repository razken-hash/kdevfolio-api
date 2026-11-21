package com.api.kdevfolio.cvgenerator.dtos;

import com.api.kdevfolio.jobapplications.enums.CvProfile;

public record GenerateCvRequest(
        CvProfile profileType,
        String jobDescription
) {}