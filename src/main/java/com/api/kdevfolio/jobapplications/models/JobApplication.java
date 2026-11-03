
package com.api.kdevfolio.jobapplications.models;

import com.api.kdevfolio.jobapplications.enums.ApplicationStatus;
import com.api.kdevfolio.jobapplications.enums.CvProfile;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JobApplication {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String companyName;

    private String offerUrl;

    @Column(nullable = false)
    private String roleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CvProfile profileType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String latexCode;

    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
