package com.api.kdevfolio.jobapplications.controllers;

import com.api.kdevfolio.jobapplications.dtos.CreateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.dtos.JobApplicationResponse;
import com.api.kdevfolio.jobapplications.dtos.UpdateApplicationStatusRequest;
import com.api.kdevfolio.jobapplications.dtos.UpdateJobApplicationRequest;
import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.services.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @GetMapping
    public List<JobApplicationResponse> findAll(
            @RequestParam(required = false) ApplicationStatus status
    ) {
        return jobApplicationService.findAll(status);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse findById(@PathVariable String id) {
        return jobApplicationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse create(@Valid @RequestBody CreateJobApplicationRequest request) {
        return jobApplicationService.create(request);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse update(
            @PathVariable String id,
            @Valid @RequestBody UpdateJobApplicationRequest request
    ) {
        return jobApplicationService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public JobApplicationResponse updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return jobApplicationService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        jobApplicationService.delete(id);
    }
}
