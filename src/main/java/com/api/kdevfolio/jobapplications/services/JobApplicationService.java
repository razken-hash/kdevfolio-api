package com.api.kdevfolio.jobapplications.services;

import com.api.kdevfolio.common.exceptions.ResourceNotFoundException;
import com.api.kdevfolio.jobapplications.dtos.CreateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.dtos.JobApplicationResponse;
import com.api.kdevfolio.jobapplications.dtos.UpdateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.mappers.JobApplicationMapper;
import com.api.kdevfolio.jobapplications.models.JobApplication;
import com.api.kdevfolio.jobapplications.repositories.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public List<JobApplicationResponse> findAll(ApplicationStatus status) {
        List<JobApplication> applications = status == null
                ? jobApplicationRepository.findAllByOrderByCreatedAtDesc()
                : jobApplicationRepository.findByStatusOrderByCreatedAtDesc(status);

        return applications.stream()
                .map(JobApplicationMapper::toResponse)
                .toList();
    }

    public JobApplicationResponse findById(String id) {
        return JobApplicationMapper.toResponse(findApplication(id));
    }

    @Transactional
    public JobApplicationResponse create(CreateJobApplicationRequest request) {
        JobApplication application = JobApplicationMapper.toEntity(
                generateApplicationId(request.companyName()),
                request
        );
        return JobApplicationMapper.toResponse(jobApplicationRepository.save(application));
    }

    @Transactional
    public JobApplicationResponse update(String id, UpdateJobApplicationRequest request) {
        JobApplication application = findApplication(id);
        JobApplicationMapper.apply(request, application);
        return JobApplicationMapper.toResponse(jobApplicationRepository.save(application));
    }

    @Transactional
    public JobApplicationResponse updateStatus(String id, ApplicationStatus status) {
        JobApplication application = findApplication(id);
        application.setStatus(status);
        return JobApplicationMapper.toResponse(jobApplicationRepository.save(application));
    }

    @Transactional
    public void delete(String id) {
        jobApplicationRepository.delete(findApplication(id));
    }

    private JobApplication findApplication(String id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application", "id", id));
    }

    /** Human-readable ID: "{company-slug}-{8 hex chars}", e.g. "acme-corp-1f3a9c0b". */
    private String generateApplicationId(String companyName) {
        String slug = Normalizer.normalize(companyName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            slug = "company";
        }

        String id;
        do {
            id = "%s-%08x".formatted(slug, ThreadLocalRandom.current().nextInt());
        } while (jobApplicationRepository.existsById(id));

        return id;
    }
}
