
package com.api.kdevfolio.jobapplications.services;

import com.api.kdevfolio.jobapplications.dtos.*;
import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.models.JobApplication;
import com.api.kdevfolio.jobapplications.repositories.JobApplicationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationResponse create(
            CreateJobApplicationRequest request
    ) {
        JobApplication application = JobApplication.builder()
                .id(generateApplicationId(request.companyName()))
                .companyName(request.companyName())
                .offerUrl(request.offerUrl())
                .roleName(request.roleName())
                .profileType(request.profileType())
                .jobDescription(request.jobDescription())
                .latexCode(request.latexCode())
                .pdfUrl(request.pdfUrl())
                .build();

        return toResponse(repository.save(application));
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findAll(
            ApplicationStatus status
    ) {
        List<JobApplication> applications =
                status == null
                        ? repository.findAllByOrderByCreatedAtDesc()
                        : repository.findByStatusOrderByCreatedAtDesc(status);

        return applications.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse findById(String id) {
        return toResponse(getApplication(id));
    }

    public JobApplicationResponse update(
            String id,
            UpdateJobApplicationRequest request
    ) {
        JobApplication application = getApplication(id);

        application.setCompanyName(request.companyName());
        application.setOfferUrl(request.offerUrl());
        application.setRoleName(request.roleName());
        application.setProfileType(request.profileType());
        application.setJobDescription(request.jobDescription());

        return toResponse(repository.save(application));
    }

    public JobApplicationResponse updateStatus(
            String id,
            ApplicationStatus status
    ) {
        JobApplication application = getApplication(id);
        application.setStatus(status);

        return toResponse(repository.save(application));
    }

    public void delete(String id) {
        repository.delete(getApplication(id));
    }

    private JobApplication getApplication(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job application not found: " + id
                ));
    }

    private String generateApplicationId(String companyName) {
        String slug = Normalizer.normalize(
                        companyName,
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            slug = "company";
        }

        String id;

        do {
            id = slug + "-" + String.format(
                    "%08x",
                    ThreadLocalRandom.current().nextInt()
            );
        } while (repository.existsById(id));

        return id;
    }

    private JobApplicationResponse toResponse(
            JobApplication application
    ) {
        return new JobApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getOfferUrl(),
                application.getRoleName(),
                application.getProfileType(),
                application.getJobDescription(),
                application.getLatexCode(),
                application.getPdfUrl(),
                application.getStatus(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
