package com.api.kdevfolio.jobapplications.repositories;

import com.api.kdevfolio.jobapplications.models.JobApplication;
import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, String> {

    List<JobApplication> findAllByOrderByCreatedAtDesc();

    List<JobApplication> findByStatusOrderByCreatedAtDesc(
            ApplicationStatus status
    );
}