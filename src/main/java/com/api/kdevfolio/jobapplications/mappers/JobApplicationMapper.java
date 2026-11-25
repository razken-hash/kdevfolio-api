package com.api.kdevfolio.jobapplications.mappers;

import com.api.kdevfolio.jobapplications.dtos.CreateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.dtos.JobApplicationResponse;
import com.api.kdevfolio.jobapplications.dtos.UpdateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.models.JobApplication;

public final class JobApplicationMapper {

    private JobApplicationMapper() {}

    public static JobApplication toEntity(String id, CreateJobApplicationRequest request) {
        JobApplication application = new JobApplication();
        application.setId(id);
        application.setCompanyName(request.companyName());
        application.setOfferUrl(request.offerUrl());
        application.setRoleName(request.roleName());
        application.setProfileType(request.profileType());
        application.setJobDescription(request.jobDescription());
        application.setLatexCode(request.latexCode());
        application.setPdfUrl(request.pdfUrl());
        return application;
    }

    public static void apply(UpdateJobApplicationRequest request, JobApplication application) {
        application.setCompanyName(request.companyName());
        application.setOfferUrl(request.offerUrl());
        application.setRoleName(request.roleName());
        application.setProfileType(request.profileType());
        application.setJobDescription(request.jobDescription());
    }

    public static JobApplicationResponse toResponse(JobApplication application) {
        return JobApplicationResponse.builder()
                .id(application.getId())
                .companyName(application.getCompanyName())
                .offerUrl(application.getOfferUrl())
                .roleName(application.getRoleName())
                .profileType(application.getProfileType())
                .jobDescription(application.getJobDescription())
                .latexCode(application.getLatexCode())
                .pdfUrl(application.getPdfUrl())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
