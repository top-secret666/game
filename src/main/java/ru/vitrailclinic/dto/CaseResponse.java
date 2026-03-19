package ru.vitrailclinic.dto;

import java.time.Instant;

public class CaseResponse {
    private Long id;
    private String status;
    private String patientAlias;
    private String ageRange;
    private String confidentialityLevel;
    private Instant createdAt;

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
