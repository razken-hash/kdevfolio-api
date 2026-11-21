package com.api.kdevfolio.cvgenerator.dtos;

import com.api.kdevfolio.jobapplications.enums.CvProfile;

public record GenerateCvResponse(
        CvProfile profileType,
        String latexCode
) {}