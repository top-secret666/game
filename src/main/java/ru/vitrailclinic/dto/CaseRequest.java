package ru.vitrailclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CaseRequest {

    @NotBlank
    @Size(max = 100)
    private String patientAlias;

    @NotBlank
    @Size(max = 50)
    private String ageRange;

    // CWE-20 fix: restrict to a known allowlist — rejects arbitrary strings and prevents business-logic bypass
    @NotBlank
    @Size(max = 50)
    @Pattern(
        regexp = "PUBLIC|RESTRICTED|CONFIDENTIAL|SECRET",
        message = "must be one of: PUBLIC, RESTRICTED, CONFIDENTIAL, SECRET"
    )
    private String confidentialityLevel;

    public String getPatientAlias() { return patientAlias; }
    public void setPatientAlias(String patientAlias) { this.patientAlias = patientAlias; }
    public String getAgeRange() { return ageRange; }
    public void setAgeRange(String ageRange) { this.ageRange = ageRange; }
    public String getConfidentialityLevel() { return confidentialityLevel; }
    public void setConfidentialityLevel(String confidentialityLevel) { this.confidentialityLevel = confidentialityLevel; }
}
