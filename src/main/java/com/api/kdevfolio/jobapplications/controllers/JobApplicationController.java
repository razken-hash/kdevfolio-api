
package com.api.kdevfolio.jobapplications.controllers;

import com.api.kdevfolio.jobapplications.dtos.*;
import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.services.JobApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-applications")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://razken-hash.site",
        "https://kenniche.abderrazak.dev"
})
public class JobApplicationController {

    private final JobApplicationService service;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(
            @Valid @RequestBody CreateJobApplicationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> findAll(
            @RequestParam(required = false) ApplicationStatus status
    ) {
        return ResponseEntity.ok(service.findAll(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> findById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateJobApplicationRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return ResponseEntity.ok(
                service.updateStatus(id, request.status())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
