package ru.vitrailclinic.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "cases")
public class CaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(name = "patient_alias")
    private String patientAlias;

    @Column(name = "age_range")
    private String ageRange;

    @Column(name = "confidentiality_level")
    private String confidentialityLevel;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (status == null) status = "DRAFT";
        if (createdAt == null) createdAt = Instant.now();
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPatientAlias() { return patientAlias; }
    public void setPatientAlias(String patientAlias) { this.patientAlias = patientAlias; }
    public String getAgeRange() { return ageRange; }
    public void setAgeRange(String ageRange) { this.ageRange = ageRange; }
    public String getConfidentialityLevel() { return confidentialityLevel; }
    public void setConfidentialityLevel(String confidentialityLevel) { this.confidentialityLevel = confidentialityLevel; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
